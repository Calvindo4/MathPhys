import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;

public class Block {
	private double x;										//the block's center y position
	private double y;										//the block's center x position
	private double width;									//the block's width
	private double height;									//the block's height
	private Wall[] sides = new Wall[4];	//the sides of the block, started from the upper side (0) in clock-wise direction
	private Color color;									//the block's color
	
	/**
	 * 
	 * @param x the x position of the block's center
	 * @param y the y position of the block's center
	 * @param width the width of the block (from left to right)
	 * @param height the height of the block (from top to bottom)
	 * @param color the color of the block
	 */
	public Block(double x, double y, double width, double height, Color color)
	{		
		//set properties
		this.x = x;
		this.y = y;
		this.color = color;
		this.width = width;
		this.height = height;
		
		//update sides
		updateSides();
	}	
	
	/**
	 * find the block's side which is the closest to the given point
	 * @param x the x position of the point of interest
	 * @param y the y position of the point of interest
	 * @return the block's side which is the closest to the given point
	 */
	public Wall closestSide(double x, double y)
	{		
		Wall closestSide = sides[0];
		double dist = closestSide.distanceLineSegment(x, y);
		double tempDist;
		for(int i=1; i<sides.length; i++)
		{
			tempDist = sides[i].distanceLineSegment(x, y);
			if(dist >= tempDist)
			{
				dist = tempDist;
				closestSide = sides[i];
			}
		}
		return closestSide;
	}
	/**
	 * find the block's side which is the closest to the given point ignoring the given side
	 * @param x the x position of the point of interest
	 * @param y the y position of the point of interest
	 * @param idxIgnoredSide the index of this block's side to be ignored
	 * @return the block's side which is the closest to the given point
	 */
	public Wall closestSide(double x, double y, int... idxIgnoredSide)
	{		
		Wall closestSide = null;
		double dist = -1;
		double tempDist = -1;
		boolean isIgnored = false;
		for(int i=0; i<sides.length; i++)
		{
			isIgnored = false;
			for(int ii=0; ii<idxIgnoredSide.length; ii++)
				if(i == idxIgnoredSide[ii])
					isIgnored = true;			
			if(isIgnored)
				continue;
			
			if(dist < 0)
			{
				dist = sides[i].distanceLineSegment(x, y);
				closestSide = sides[i];
				continue;
			}
			
			tempDist = sides[i].distanceLineSegment(x, y);
			if(dist >= tempDist)
			{
				dist = tempDist;
				closestSide = sides[i];
			}
		}
		return closestSide;
	}
	
	public double getX(){return x;}
	public double getY(){return y;}
	public double getWidth(){return width;}
	public double getHeight(){return height;}
	public void setX(double x){
		this.x = x;
		updateSides();
	}
	public void setY(double y){
		this.y = y;
		updateSides();
	}
	public void updateSides()
	{
		//from left to right (north side)
		if(sides[0] != null)
			sides[0].setPoints(x-width/2, y+height/2, x+width/2, y+height/2);
		else
			sides[0] = new Wall(x - width / 2, y + height / 2, x + width / 2, y + height / 2, color);

		//from top to bottom (east side)
		if(sides[1] != null)
			sides[1].setPoints(x+width/2, y+height/2, x+width/2, y-height/2);			
		else
			sides[1] = new Wall(x + width / 2, y + height / 2, x + width / 2, y - height / 2, color);
										
		//from right to left (south side)
		if(sides[2] != null)
			sides[2].setPoints(x+width/2, y-height/2, x-width/2, y-height/2);
		else
			sides[2] = new Wall(x + width / 2, y - height / 2, x - width / 2, y - height / 2, color);
		
		//from bottom to top (west side)
		if(sides[3] != null)
			sides[3].setPoints(x-width/2, y-height/2, x-width/2, y+height/2);
		else			
			sides[3] = new Wall(x - width / 2, y - height / 2, x - width / 2, y + height / 2, color);
	}
	
	public void draw(Graphics g, int originX, int originY, double scale)
	{
		Color tempColor = g.getColor();
		g.setColor(color);
		g.fillRect((int)(originX + (x-width/2)*scale), (int)(originY - (y+height/2)*scale), (int)(width*scale), (int)(height*scale));		
		g.setColor(tempColor);
	}
}