package ch.weiss;

public class Position
{
  private final double x;
  private final double y;

  public Position(double x, double y)
  {
    this.x = x;    
    this.y = y;    
  }

  public Position(int x, int y)
  {
    this.x = x;    
    this.y = y;    
  }

  public Position add(RelativePosition operand)
  {
    return new Position(x+operand.getX(), y+operand.getY());
  }

  public RelativePosition minus(Position operand)
  {
    return RelativePosition.withCartesian(x-operand.x, y-operand.y);
  }
  
  @Override
  public String toString()
  {
    return "["+(int)x+", "+(int)y+"]";
  }

  public Position normalize(Position maxPosition)
  {
    double newX = Math.max(x, 0.0d);
    newX = Math.min(newX, maxPosition.x);
    double newY = Math.max(y, 0.0d);
    newY = Math.min(newY, maxPosition.y);
    return new Position(newX, newY);
  }

  public double getX()
  {
    return x;
  }

  public int getXAsInt()
  {
    return (int)x;
  }

  public double getY()
  {
    return y;
  }
  
  public int getYAsInt()
  {
    return (int)y;
  }

  public Position add(Position position)
  {
    return new Position(x+position.x, y+position.y);
  }

}
