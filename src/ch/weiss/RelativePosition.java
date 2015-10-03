package ch.weiss;

public class RelativePosition
{
  private static final double TWO_PI = 2*Math.PI;
  private final double direction;
  private final double distance;

  private RelativePosition(double direction, double distance)
  {
    this.direction = direction;
    this.distance = distance;
  }

  public double getX()
  {
    return Math.sin(direction)*distance;
  }

  public double getY()
  {
    return Math.cos(direction)*distance;
  }
  
  public double getDirection()
  {
    return direction;
  }
  
  public double getDistance()
  {
    return distance;
  }

  public static RelativePosition withCartesian(double x, double y)
  {
    double phi = Math.atan(x/y);
    if (y < 0.0d)
    {
      phi = phi + Math.PI;
    }
    else if (x < 0.0d)
    {
      phi = phi + TWO_PI;
    }
    return new RelativePosition(phi, Math.hypot(x, y));
  }

  public static RelativePosition withPolar(
      double direction, double distance)
  {
    if (direction < 0.0d || direction >= TWO_PI)
    {
      throw new IllegalArgumentException("Parameter direction must be between 0 and 2 PI but was "+direction);
    }
    return new RelativePosition(direction, distance);
  }

}
