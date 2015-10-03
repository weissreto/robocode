package ch.weiss;

import java.awt.Graphics2D;

public enum RobotState
{
  SEARCH 
  {
    public void onPaint(Graphics2D g, Position position)
    {
      g.drawLine(position.getXAsInt(), position.getYAsInt(), position.getXAsInt()+10, position.getYAsInt()-5);
      g.drawLine(position.getXAsInt(), position.getYAsInt(), position.getXAsInt()+10, position.getYAsInt()+5);
    }

  }, 
  
  FLEE 
  {
    public void onPaint(Graphics2D g, Position position)
    {
      g.drawLine(position.getXAsInt(), position.getYAsInt(), position.getXAsInt()+10, position.getYAsInt());
      g.drawLine(position.getXAsInt(), position.getYAsInt(), position.getXAsInt()+5, position.getYAsInt()+5);
      g.drawLine(position.getXAsInt(), position.getYAsInt(), position.getXAsInt()+5, position.getYAsInt()-5);
    }

  }, 
  
  ATTACK 
  {
    public void onPaint(Graphics2D g, Position position)
    {
      g.drawLine(position.getXAsInt()-5, position.getYAsInt(), position.getXAsInt()+5, position.getYAsInt());
      g.drawLine(position.getXAsInt(), position.getYAsInt()-5, position.getXAsInt(), position.getYAsInt()+5);
      g.drawOval(position.getXAsInt()-5, position.getYAsInt()-5, 10, 10);      
    }
  };

  abstract public void onPaint(Graphics2D g, Position position);

 
}
