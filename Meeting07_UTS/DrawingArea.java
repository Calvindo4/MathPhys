import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DrawingArea extends JPanel
{
	Block controlledBlock;
	Wall[] walls = new Wall[4];
	private int height;
	private int width;
	private boolean press = false;
	private ArrayList<Block> targets;
	private Thread animator;
	private BufferedImage dbImage;

	Ball ball;

	double arenaX1 = -15;
	double arenaY1 = 10;
	double arenaX2 = 15;
	double arenaY2 = -10;
	double maxNumX = 20;

	double vX = 0.1;

	double scale;

	private int originX;	
	private int originY;



	public DrawingArea(int width, int height, Ball ball, Wall wall[], Block controlledBlock, ArrayList<Block> targets)
	{
		super(null);

		originX = width/2;
		originY = height/2;
		scale = (double)(width-originX)/(maxNumX);

		this.height = height;
		this.width = width;
		this.ball = ball;
		setBounds(0,0,width,height);
		this.targets = targets;
		this.walls = wall;
		this.controlledBlock = controlledBlock;
		animator = new Thread(this::eventLoop);
	}
	
	public void start()
	{
		animator.start();
	}

	public void detectCollision(Ball ball, Block block)
	{		
		//check if the returned side is the correct one based on ball's velocity
		Wall closestSide = null;
		if(ball.getVx() >= 0 && ball.getVy() >= 0)
			closestSide = block.closestSide(ball.getX(), ball.getY(), 0, 1);
		else if(ball.getVx() >= 0 && ball.getVy() < 0)			
			closestSide = block.closestSide(ball.getX(), ball.getY(), 1, 2);
		else if(ball.getVx() < 0 && ball.getVy() >= 0)
			closestSide = block.closestSide(ball.getX(), ball.getY(), 0, 3);
		else if(ball.getVx() < 0 && ball.getVy() < 0)
			closestSide = block.closestSide(ball.getX(), ball.getY(), 2, 3);			
		detectCollision(ball, closestSide);					
	}

	public void detectCollision(Ball ball, Wall wall)
	{
		double dist = wall.distanceLineSegment(ball.getX(), ball.getY()); 
		if(dist <= ball.getR())
		{
			//get the normal line of the wall
			Vector normWall = wall.normalLine();
			
			//dot product with ball's velocity
			double dotProduct = normWall.getX() * ball.getVx() + normWall.getY() * ball.getVy();
			
			//new velocity
			double newVx = -2*dotProduct*normWall.getX() + ball.getVx();
			double newVy = -2*dotProduct*normWall.getY() + ball.getVy();
			
			//calculate distance between ball and wall				
			double error = Math.abs(dist - ball.getR());
			double oldV = Math.sqrt(ball.getVx()*ball.getVx() + ball.getVy()*ball.getVy());
			ball.move(ball.getX() - ball.getVx()/oldV*error, ball.getY() - ball.getVy()/oldV*error);

			//set the ball's velocity
			ball.setVx(newVx);
			ball.setVy(newVy);
		}				
	}

	public void eventLoop()
	{
		dbImage = (BufferedImage) createImage(width,height);
		while(true)
		{
			update();
			render();
			printScreen();
			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException ex)
			{
				ex.printStackTrace();
				break;
			}
		}
	}
	public void run() 
	{
		while(true)
		{
			update();
			render();
			printScreen();
		}		
	}
	
	
	public void update()
	{	
		//move the controlled block
		if(controlledBlock.getX() + controlledBlock.getWidth()/2 >= arenaX2)			
		{
			vX = -vX;
			controlledBlock.setX(arenaX2 - controlledBlock.getWidth()/2);
		}
		if(controlledBlock.getX() - controlledBlock.getWidth()/2 <= arenaX1)
		{
			vX = -vX;
			controlledBlock.setX(arenaX1 + controlledBlock.getWidth()/2);
		}
		controlledBlock.setX(controlledBlock.getX() + vX);
					
		//move the ball
		ball.move();	
		
		//detect collision between ball and walls
		for(int i=0; i<walls.length; i++)
			detectCollision(ball, walls[i]);			
		
		//detect collision between ball and controlled block
		detectCollision(ball, controlledBlock);
					
		//detect collision between ball and targets
		for(Block t: targets)
			detectCollision(ball, t);
						
	}
	
	public void render()
	{
		if(dbImage != null)
		{				
			//get graphics of the image where coordinate and function will be drawn
			Graphics g = dbImage.getGraphics();
		
			//clear screen
			g.setColor(Color.white);
			g.fillRect(0, 0, width, height);
							
			//draw the ball
			ball.draw(g, originX, originY, scale);
			
			//draw the controlled block
			controlledBlock.draw(g, originX, originY, scale);
			
			//draw the arena
			for(int i=0; i<walls.length; i++)
				walls[i].draw(g, originX, originY, scale);
			
			//draw the targets
			for(Block t: targets)
				t.draw(g, originX, originY, scale);
		}
	}
	
	public void printScreen()
	{
		try
		{
			Graphics g = getGraphics();
			if(dbImage != null && g != null)
			{
				g.drawImage(dbImage, 0, 0, null);
			}
			
			// Sync the display on some systems.
			// (on Linux, this fixes event queue problems)
			Toolkit.getDefaultToolkit().sync();
			g.dispose();
		}
		catch(Exception ex)
		{
			System.out.println("Graphics error: " + ex);  
		}		
	}

}