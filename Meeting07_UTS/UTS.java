import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

import javax.swing.JFrame;

/*
	Matfis UTS
	Brick breaker
	TODO:
	 1. Separate drawing area from frame
     2. Make more brick targets
     3. Increase brick size and increase gaps among bricks
     4. Enlarge ball and change all colors in the game system
     5. Create point system
	 6. Make controlledBlock controllable by keyboard only
	 7. Make controlledBlock controllable by mouse only
     8. Make a multiplayer version (reference: Gamehouse's Hamsterball multiplayer)
 */


public class UTS {
	//Thread where animation is run
	private JFrame frame;
	
	//The blocks to be drawn
	Block controlledBlock;
	ArrayList<Block> targets = new ArrayList<>();
	
	//the ball to be drawn
	Ball ball;
	
	//the walls (arena)
	Wall[] walls = new Wall[4];

	
	//Variables used for the ball speed		
	private double v;
	
	//Variables used for the arena
	double arenaX1;
	double arenaY1;
	double arenaX2;
	double arenaY2;
	
	//Variables used for the controlled block
	double vX;
	double blockWidth;
	double blockHeight;
	
	//Variables used for the blocks to be hit
	int ttlPerRow;
	int ttlPerCol;
	double targetWidth;
	double targetHeight;
	
	//Variables used to draw the xy-coordinate  
	private double scale;	// the number of pixels for each one step (the distance between two numbers / steps)
	private double maxNumX;	// total numbers (steps) in the x-axis
	private double maxNumY;	// total numbers (steps) in the y-axis
	private double maxX;	// total numbers (steps) in the x-axis
	private double maxY;	// total numbers (steps) in the y-axis	
	private int originX;	// the x origin
	private int originY;	// the y origin
		
	/**
	 * 
	 */
	public UTS()
	{
		//configure the main canvas
		frame = new JFrame();
		frame.setSize(1366, 768);
		frame.setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setLayout(null);
		frame.setVisible(true);
		
		//setting the point of origin of the coordinate
		originX = frame.getWidth()/2;
		originY = frame.getHeight()/2;
		
		//maximal numbers (steps) on x-axis and y-axis
		maxNumX = 20;
		scale = (double)(frame.getWidth()-originX)/(maxNumX);
		maxNumY = (int) ((frame.getHeight()-originY)/scale);

		//calculating the maximum value in the x-axis and y-axis
		maxX = (frame.getWidth() - originX)/scale;
		maxY = (frame.getHeight() - originY)/scale;
					
		//create the ball (x-axis,y-axis,radius,vx,vy,color) in cartesian scale
		v = -0.3 + Math.random()*0.3;
		ball = new Ball(0, 0, 1.4, v, v, Color.BLUE);
		
		//create the arena
		arenaX1 = -15; arenaX2 = 15;
		arenaY1 = 10; arenaY2 = -10;			
		walls[0] = new Wall(arenaX2, arenaY1, arenaX1, arenaY1, Color.RED);	//north wall (right to left)
		walls[1] = new Wall(arenaX2, arenaY2, arenaX2, arenaY1, Color.RED);	//east wall (bottom to top)
		walls[2] = new Wall(arenaX1, arenaY2, arenaX2, arenaY2, Color.RED);	//south wall (left to right)
		walls[3] = new Wall(arenaX1, arenaY1, arenaX1, arenaY2, Color.RED);	//west wall (top to bottom)

		//create the block to be controlled
		vX = 0.1;
		blockWidth = 3.0;
		blockHeight= 1.0;
		controlledBlock = new Block(0, -6, blockWidth, blockHeight, Color.GRAY);
		
		Random randomGenerator = new Random();

		//setting the position for the blocks to be hit
		ttlPerRow = 10;
		ttlPerCol = 1;
		targetWidth = 1; targetHeight= 0.5;
		double targetX, targetY, incX, incY, leftMostX, rightMostX, topY, bottomY;
		leftMostX = arenaX1 + 0.5 + targetWidth/2; rightMostX = arenaX2 - 0.5 - targetWidth/2;
		topY = arenaY1 - 1; bottomY = 0;
		targetX = leftMostX; targetY = topY;
		incX = ((rightMostX - leftMostX) / (ttlPerRow-1));
		incY = 0;

		double gap = 0.0;
		//create the blocks to be hit
		for(int i=0; i<ttlPerRow; i++)
		{	
			for(int j=0; j<ttlPerCol; j++)
			{	
				Color color = new Color(randomGenerator.nextInt(225), randomGenerator.nextInt(225), randomGenerator.nextInt(225));
				targets.add(new Block(targetX + i * incX - gap, targetY + i * incY, targetWidth * 1.8, targetHeight, color));
				gap += 0.3;
			}
		}
		
		//start the thread to draw functions to canvas
		DrawingArea drawingArea = new DrawingArea(frame.getWidth(), frame.getHeight(), ball, walls, controlledBlock, targets);
		frame.add(drawingArea);
		drawingArea.start();
	}
		
	public static void main(String[] args) {
		EventQueue.invokeLater(UTS::new);
	}
}