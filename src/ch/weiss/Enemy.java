package ch.weiss;

import java.awt.Color;
import java.awt.Graphics2D;

import robocode.Rules;

public class Enemy
{
  private String name;
  private double heading;
  private Position position;
  private double velocity;
  private RelativePosition relativePosition;
  private Position battleFieldMaxPosition;
  private long time;

  public Enemy(String name, Position battleFieldMaxPosition)
  {
    this.name = name;
    this.battleFieldMaxPosition = battleFieldMaxPosition;
  }

  public void scanned(Position robotPosition, RelativePosition enemyRelPos, double heading, double velocity, long time)
  {
    this.relativePosition = enemyRelPos;
    this.position = robotPosition.add(enemyRelPos);
//    System.out.println("Enemy Position: "+position);
//    System.out.println("Enemy Heading: "+Math.toDegrees(heading));
//    System.out.println("Enemy Velocity: "+velocity);
    this.heading = heading;
    this.velocity = velocity;
    this.time = time;
  }

  public double getTargetDirection(Position robotPosition, long time)
  {
    Position newPosition = getTargetPosition(time);
//    System.out.println("Bullet Velocity: "+bulletVelocity);
//    System.out.println("Bullet Flight Time: "+bulletFlightTime);
//    System.out.println("Distance: "+distance);
//    
//    System.out.println("Target Position: "+newPosition);
    return newPosition.minus(robotPosition).getDirection();
  }

  private Position getTargetPosition(long time)
  {
    double bulletVelocity = Rules.getBulletSpeed(getFirePower());
    double bulletFlightTime = relativePosition.getDistance() / bulletVelocity;
    long deltaTime = time-this.time;
    double targetTime = deltaTime + bulletFlightTime;
    double distance = targetTime*velocity;
    Position newPosition = position.add(RelativePosition.withPolar(heading, distance));
    newPosition = newPosition.normalize(battleFieldMaxPosition);
    return newPosition;
  }

  public double getFirePower()
  {    
    return 400.0d/relativePosition.getDistance();
  }

  public boolean isNearerThan(Enemy nearest, Position robotPosition)  
  {
    if (nearest == null)
    {
      return true;
    }
    return relativePosition.getDistance() < nearest.relativePosition.getDistance();
  }

  public double getDistance()
  {
    return relativePosition.getDistance();
  }

  public void onPaint(Graphics2D g, long time)
  {
    g.setColor(Color.RED);
    g.drawOval(position.getXAsInt()-20, (int)position.getYAsInt()-20, 40, 40);
    RelativePosition headingVector = RelativePosition.withPolar(heading, velocity*5.0d);
    Position headingTo = position.add(headingVector);
    g.drawLine(position.getXAsInt(), position.getYAsInt(), headingTo.getXAsInt(), headingTo.getYAsInt());
    Position targetPosition = getTargetPosition(time);
    g.drawLine(targetPosition.getXAsInt()-5, targetPosition.getYAsInt(), targetPosition.getXAsInt()+5, targetPosition.getYAsInt());
    g.drawLine(targetPosition.getXAsInt(), targetPosition.getYAsInt()-5, targetPosition.getXAsInt(), targetPosition.getYAsInt()+5);
    g.drawOval(targetPosition.getXAsInt()-5, targetPosition.getYAsInt()-5, 10, 10);
  }

}
