import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;



public class Ball {
	private double xd, yd;
	private double x,y,r;
	private double vx, vy;	
	private Color ballColor;
	public static final double E = 0.5;
	
	/**
	 * 
	 * @param x: the first x coordinate of the ball (in CARTESIAN coordinate) 
	 * @param y: the first y coordinate of the ball (in CARTESIAN coordinate)
	 * @param r: the radius of the ball (in CARTESIAN coordinate)
	 * @param vx: the velocity of the ball in x-axis
	 * @param vy: the velocity of the ball in y-axis
	 * @param ballColor: the color of the ball to be drawn
	 */
	public Ball(double x, double y, double r, double vx, double vy, Color ballColor)
	{
		//setting the r of the ball, both the real r or the r used to draw (width and height of oval)
		this.r = r*1.4;		
		
		//setting the x coordinate of the ball, both the real x (the center of the ball) or the x used to draw (the left-most x)
		this.x = x;
		xd = x - r;
		
		//setting the y coordinate of the ball, both the real y (the center of the ball) or the y used to draw (the left-most x)
		this.y = y;
		yd = y + r;
				
		this.vx = vx;
		this.vy =vy;		
		this.ballColor = ballColor;
	}
	
	/**
	 * funtion to draw the ball on the given graphics 
	 * @param g: the graphics where the ball is drawn
	 * @param originX: the x coordinate of the origin point in the canvas (in canvas PIXELS)
	 * @param originY: the y coordinate of the origin point in the canvas (in canvas PIXELS) 
	 * @param scale: the number of PIXELS for each step in the CARTESIAN coordinate
	 */
	public void draw(Graphics g, double originX, double originY, double scale)
	{		
		Color tempColor = g.getColor();
		g.setColor(ballColor);
		g.fillOval((int)(originX + (xd)*scale), (int)(originY - (yd)*scale), (int)(r*2*scale), (int)(r*2*scale));
		g.setColor(Color.BLACK);
		g.drawString(".", (int)(originX + (x)*scale), (int)(originY - (y)*scale));
		g.setColor(tempColor);
	}
	
	/**
	 * funtion to move the ball's position in cartesian coordinate based on its velocity
	 */
	public void move()
	{			
		x += vx;		
		xd = x - r;
		y += vy;	
		yd = y + r;
	}
	/**
	 * funtion to move the ball's position in cartesian coordinate based on its velocity
	 */
	public void move(double newX, double newY)
	{
		x = newX;
		xd = x - r;
		y = newY;	
		yd = y + r;
	}
	
	/**
	 * function to return the ball's radius
	 * @return the ball's radius
	 */
	public double getR()
	{
		return r;
	}
	
	/**
	 * function to return the y position used to draw the circle
	 * @return a double value indicating the y-position used to draw the circle
	 */
	public double getYd()
	{
		return yd;
	}	
	/**
	 * function to return the real y position of the circle
	 * @return a double value indicating the real y-position of the circle's center
	 */
	public double getY()
	{
		return y;
	}
	/**
	 * function to return the velocity of the circle in y-axis
	 * @return a double value indicating the velocity of the circle in y-axis
	 */
	public double getVy()
	{
		return vy;
	}
	
	/**
	 * function to return the x position used to draw the circle
	 * @return a double value indicating the x-position used to draw the circle
	 */
	public double getXd()
	{
		return xd;
	}	
	/**
	 * function to return the real x position of the circle
	 * @return a double value indicating the real x-position of the circle's center
	 */
	public double getX()
	{
		return x;
	}
	/**
	 * function to return the velocity of the circle in x-axis
	 * @return a double value indicating the velocity of the circle in x-axis
	 */
	public double getVx()
	{
		return vx;
	}
	
	/**
	 * function to set the velocity of the circle in y-axis
	 */
	public void setVy(double vy)
	{
		this.vy = vy;
	}
	/**
	 * function to set the velocity of the circle in x-axis
	 */
	public void setVx(double vx)
	{
		this.vx = vx;
	}

	
}