import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;

public class Wall {
	private Color wallColor;
	private double x1, y1, x2, y2;
	
	/**
	 * 
	 * @param x1 the x position of the first point in the cartesian coordinate
	 * @param y1 the y position of the first point in the cartesian coordinate
	 * @param x2 the x position of the second point in the cartesian coordinate
	 * @param y2 the y position of the second point in the cartesian coordinate
	 * @param wallColor the color of the line to be drawn
	 */
	public Wall(double x1, double  y1, double  x2, double  y2, Color wallColor)
	{
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;		
		this.wallColor = wallColor;
	}
		
	/**	 
	 * @param g: graphics object where the wall should be drawn
	 * @param originX: the x position in the canvas (in pixels) where the point of origin in cartesian coordinate is
	 * @param originY: the y position in the canvas (in pixels) where the point of origin in cartesian coordinate is
	 */
	public void draw(Graphics g, double originX, double originY, double scale)
	{
		Color tempColor = g.getColor();
		g.setColor(wallColor);
		g.drawLine((int)(x1*scale + originX),(int)(originY - (y1)*scale),(int)(originX + x2*scale),(int)(originY - (y2)*scale));
		g.setColor(tempColor);
	}

	/**
	 * function to calculate the normal line of the wall in unit vector
	 * the direction of the normal line depends on the x1, x2, y1, and y2
	 * to change its direction, times the resultant vector with -1
	 * @return a point representing a vector with its x and y component
	 */
	public Vector normalLine()
	{
		//normal vector to be returned
		Vector normVector = new Vector();
		double normVectorX, normVectorY;
		//wall vector
		Vector wallVector = new Vector();
		wallVector.setX(x2-x1);
		wallVector.setY(y2-y1);
		
		//calculate the normal vector, then calculate the unit vector
		normVectorY = wallVector.getX();
		normVectorX = (-1)*(wallVector.getY());
		double normVectorLength = Math.sqrt(normVectorX*normVectorX + normVectorY*normVectorY);		
		normVector.setX(normVectorX/normVectorLength);
		normVector.setY(normVectorY/normVectorLength);
		return normVector;
	}
	
	/**
	 * calculate the distance between this wall (probably a line segment) with the given point
	 * @param xPoint the x position of the given point
	 * @param yPoint the y position of the given point
	 * @return the distance between this wall with the given point
	 */
	public double distanceLineSegment(double xPoint, double yPoint)
	{
		double dot = dotLineSegment(xPoint, yPoint);
		double distX, distY;
		if(dot < 0)
		{
			distX = x1 - xPoint;
			distY = y1 - yPoint;
			return Math.sqrt(distX*distX + distY*distY);
		}
		else if(dot > 1)
		{
			distX = x2 - xPoint;
			distY = y2 - yPoint;
			return Math.sqrt(distX*distX + distY*distY);
		}
		else
		{
			return distance(xPoint, yPoint);
		}
	}
	
	/**
	 * calculate the dot product between the 1-unit wall vector (from point 1 to point 2) 
	 * and the given point (from point 1 to the-given point)
	 * @param xPoint the x position of the given point
	 * @param yPoint the y position of the given point
	 * @return the dot product between the 1-unit wall vector (from point 1 to point 2) and the given point (from point 1 to the-given point)
	 */
	private double dotLineSegment(double xPoint, double yPoint)
	{
		double wallX = x2 - x1;
		double wallY = y2 - y1;
		double wallToPointX = xPoint - x1;
		double wallToPointY = yPoint - y1;
		
		double lengthWall = wallX*wallX + wallY*wallY;
		double dot = ((wallX*wallToPointX)+(wallY*wallToPointY))/lengthWall;
		return dot;
	}
	
	/**
	 * function to calculate the minimal distance between this wall and a point		 
	 * Calculate the distance using this formula:
	 * 		|(a*xPoint) + (b*yPoint) + (b*y1 - a*x1)| / (sqrt(a^2 + b^2))
	 * @param xPoint: the x coordinate of the point whose distance want to be calculated
	 * @param yPoint: the y coordinate of the point whose distance want to be calculated
	 * @return the distance between the point and the line
	 */
	private double distance(double xPoint, double yPoint)
	{
		double distance, b, a;
		b = (x2-x1);
		a = (y2-y1);
		distance = Math.abs(a*xPoint + (-b)*yPoint + (b*y1 - a*x1));
		distance = distance / Math.sqrt(a*a + b*b);
		return distance;		
	}

	/**
	 * function to calculate the point in the line which is the nearest from the given point
	 * @param xPoint the x coordinate of the point given
	 * @param yPoint the y coordinate of the point given
	 * @return a point in the line which is the nearest from the (xPoint,yPoint)
	 */
	public Vector nearestPoint(double xPoint, double yPoint)
	{
		//point to be returned
		Vector nearestPoint = new Vector();
		//the x and y coordinate in the line which is nearest from the point given 
		double nearestX, nearestY;
		
		//calculating the nearest x and y coordinate
		double a = y2-y1, b = x2-x1;
		nearestX = (-b*(-b*xPoint - a*yPoint) - a*(b*y1 - a*x1));
		nearestX = nearestX/(a*a + b*b);
		nearestY = (a*(b*xPoint + a*yPoint) - (-b)*(b*y1 - a*x1));
		nearestY = nearestY/(a*a + b*b);
		
		//return the point after setting it up
		nearestPoint.setX(nearestX);
		nearestPoint.setY(nearestY);
		return nearestPoint;
	}
	
	public double getDY()
	{
		return y2-y1;
	}
	
	public double getDX()
	{
		return x2-x1;
	}
	
	public double getX1()
	{
		return x1;
	}
	public double getX2()
	{
		return x2;
	}
	public double getY1()
	{
		return y1;
	}
	public double getY2()
	{
		return y2;
	}
	public void setX1(double x1){this.x1 = x1;}
	public void setX2(double x2){this.x2 = x2;}
	public void setY1(double y1){this.y1 = y1;}
	public void setY2(double y2){this.y2 = y2;}
	public void setPoints(double x1, double y1, double x2, double y2)
	{
		setX1(x1); setX2(x2); setY1(y1); setY2(y2);
	}	
}
