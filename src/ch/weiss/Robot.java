package ch.weiss;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Robot extends AdvancedRobot
{
  private Map<String, Enemy> enemies = new HashMap<>();
  private double hitCount = 0;
  private RobotState state = RobotState.SEARCH;

  @Override
  public void run()
  {
    setAdjustGunForRobotTurn(false);
    setTurnRadarLeft(Double.MAX_VALUE);
    while (true)
    {
      if (hitCount > 0.0d)
      {
        hitCount = hitCount-0.01d;         
      }
      if (hitCount > 10.0d*getEnergy()/100)
      {        
        flee();
      }
      else
      {
        Enemy enemy = getNearestEnemy();
        if (enemy != null)
        {
          attack(enemy);        
        }
        else
        {
          search();
        }
      }
    }
  }

  private void search()
  {
    state = RobotState.SEARCH;
    scan();
  }

  private void flee()
  {
    state = RobotState.FLEE;
    hitCount = 0.0d;
    System.out.println(">>> Flee");
    double fleeDirection;
    Position pos = getPosition();
    if (pos.getX() < 100.0d)
    {
      fleeDirection = Math.PI/2;
    }
    else if (pos.getX() + 100.0d > getBattleFieldMaxPosition().getX())
    {
      fleeDirection = Math.PI*3/2;
    }
    else if (pos.getY() < 100.0d)
    {
      fleeDirection = 0.0d;
    }
    else if (pos.getY() + 100.0d > getBattleFieldMaxPosition().getY())
    {
      fleeDirection = Math.PI;
    }
    else
    {
      fleeDirection = Utils.getRandom().nextBoolean()?-1.0:1.0;
      fleeDirection = fleeDirection* Math.PI/2.0d;      
      fleeDirection = Utils.normalAbsoluteAngle(getHeadingRadians()-fleeDirection);
    }
    
    double turn = Utils.normalRelativeAngle(fleeDirection-getHeadingRadians());
    setTurnRightRadians(turn);
    ahead(200);
  }

  private void attack(Enemy enemy)
  {
    state = RobotState.ATTACK;
    double targetDirection = enemy.getTargetDirection(getPosition(), getTime());
    double gunRight = Utils.normalRelativeAngle(targetDirection - getGunHeadingRadians());
    setTurnGunRightRadians(gunRight);
    if (getGunHeat()<= 0 && getGunTurnRemaining() < 1)
    {
      fire(enemy.getFirePower());
    }
    else
    {
      execute();
    }
    double robotRight = Utils.normalRelativeAngle(targetDirection - getHeadingRadians()) / 10;
    setTurnRightRadians(robotRight);
    if (enemy.getDistance() > 100.0d)
    {
      double distance = (enemy.getDistance()-100.0d) / 2;
      setAhead(distance);
    }
    else
    {
      double distance = (100.0d - enemy.getDistance()) / 2;
      setBack(distance);
    }
  }

  @Override
  public void onScannedRobot(ScannedRobotEvent event)
  {
//    System.out.println("=====================");
//    System.out.println("Bearing: " + event.getBearing());
//    System.out.println("Gun Heading: " + getGunHeading());
//    System.out.println("Heading: " + getHeading());
//    System.out.println("Position: "+getPosition());
    double enemyDirection = Utils.normalAbsoluteAngle(getHeadingRadians()
        + event.getBearingRadians());
//    System.out.println("Enemy Direction: " + Math.toDegrees(enemyDirection));

    Enemy enemy = getEnemy(event.getName());
    Position position = getPosition();
    RelativePosition enemyRelPos = RelativePosition.withPolar(enemyDirection, event.getDistance());

    enemy.scanned(position, enemyRelPos, event.getHeadingRadians(),
        event.getVelocity(), event.getTime());
  }
  
  @Override
  public void onRobotDeath(RobotDeathEvent event)
  {
    removeEnemy(event.getName());
  }
  
  @Override
  public void onHitByBullet(HitByBulletEvent event)
  {
    hitCount = hitCount+event.getPower();
  }
  
  public void onPaint(Graphics2D g)
  {
    Position position = getPosition();
    g.setColor(Color.GREEN);
    g.drawOval(position.getXAsInt()-20, (int)position.getYAsInt()-20, 40, 40);
    
    RelativePosition direction = RelativePosition.withPolar(getHeadingRadians(), getVelocity()*5);
    Position headingTo = position.add(direction);
    g.drawLine(position.getXAsInt(), position.getYAsInt(), headingTo.getXAsInt(), headingTo.getYAsInt());
    
    RelativePosition gunDirection = RelativePosition.withPolar(getGunHeadingRadians(), 10000.0d);
    Position gunPosition = position.add(gunDirection);
    g.drawLine(position.getXAsInt(), position.getYAsInt(), gunPosition.getXAsInt(), gunPosition.getYAsInt());
    
    position = position.add(new Position(-20, 20));
    state.onPaint(g, position);
    for (Enemy enemy : enemies.values())
    {
      enemy.onPaint(g, getTime());
    }
    super.onPaint(g);
  }

  private void removeEnemy(String name)
  {
    enemies.remove(name);
  }

  public Position getPosition()
  {
    return new Position(getX(), getY());
  }

  private Enemy getEnemy(String name)
  {
    Enemy enemy = enemies.get(name);
    if (enemy == null)
    {
      enemy = new Enemy(name, getBattleFieldMaxPosition());
      enemies.put(name, enemy);
    }
    return enemy;
  }
  
  private Enemy getNearestEnemy()
  {
    Enemy nearest = null;
    for (Enemy enemy : enemies.values())
    {
      if (enemy.isNearerThan(nearest, getPosition()))
      {
        nearest = enemy;
      }
    }
    return nearest;
  }
  
  

  private Position getBattleFieldMaxPosition()
  {
    return new Position(getBattleFieldWidth(), getBattleFieldHeight());
  }
}
