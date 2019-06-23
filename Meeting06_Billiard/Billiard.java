//package Meeting06_Billiard;

/*
	Matfis pertemuan 6
	Half-fledged billiard

	TODO:
	 1. Create billiard balls, use 8-ball rules.
	 2. Assign one ball to be the hitter (preferably not colored white)
	 3. Create guideline to help aiming the hitter (refer to #04 Clasher)
	 4. Add collision for ball against wall and ball against balls.
	 5. Create holes for the balls
	 6. Add additional mechanics for the game and scoring system
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class Billiard {
	private JFrame frame;
	private int frameHeight;

	//The collections of walls to be drawn
	private ArrayList<Wall> walls = new ArrayList<>();
	private ArrayList<Ball> balls = new ArrayList<>();

	private Ball hitter;
	private Vector destination;

	private Billiard() {
		//configure the main canvas
		frame = new JFrame("Billiard");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setVisible(true);
		frameHeight = frame.getHeight() - frame.getInsets().top;

		createObjects();

		hitter = new Ball((frame.getWidth() * 0.66) - 400.0, frame.getHeight() / 2.0, Color.gray);	//Create hitter
		balls.add(hitter);	//Add hitter to ball array
		destination = new Vector(hitter.getPositionX(), hitter.getPositionY());		//Get destination coordinates

		DrawingArea drawingArea = new DrawingArea(frame.getWidth(), frameHeight, balls, walls, balls.size() - 1, destination);
		frame.add(drawingArea);

		//Mouse Listeners
		frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                drawingArea.setPress(true);
                destination.setX((double) e.getX());
                destination.setY((double) e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                double distanceX = e.getX() - hitter.getPositionX();
                double distanceY = e.getY() - hitter.getPositionY();
                double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                hitter.setVelocityX(drawingArea.getTime() * distanceX / distance);
                hitter.setVelocityY(drawingArea.getTime() * distanceY / distance);
                //test.setText(Double.toString(hitter.getVelocityX()));

                drawingArea.setPress(false);
            }
        });

        frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                destination.setX((double) e.getX());
                destination.setY((double) e.getY());
            }
        });

		drawingArea.start();
	}


	private void createObjects() {
		int wallWidth = (int) (frame.getWidth() * 0.9);
		int wallHeight = (int) (frameHeight * 0.6);
		int wallX = (int) (frame.getWidth() * 0.05);
		int wallY = (int) (frameHeight * 0.2);

		// vertical wall must be defined in clockwise direction
		// horizontal wall must be defined in counter clockwise direction
		walls.add(new Wall(wallWidth + wallX, wallY, wallX, wallY));	// top wall
		walls.add(new Wall(wallX, wallHeight + wallY, wallX, wallY));	// left wall
		walls.add(new Wall(wallWidth + wallX, wallY, wallWidth + wallX, wallHeight + wallY));	// bottom wall
		walls.add(new Wall(wallWidth + wallX, wallHeight + wallY, wallX, wallHeight + wallY));	// right wall

		Random randomGenerator = new Random();
	
		double x = frame.getWidth() * 0.66;     //Starting x-axis
		double cy  = frame.getHeight() / 2.0;   //Starting y-axis
		
		createBalls(x,cy);
	}

  //Function to create balls
	public void createBalls(double x, double cy)
	{
		double y;
		Random randomGenerator = new Random();
		for (int i = 1; i <= 5; i++) 
		{
			y = cy;
			x = x + (Ball.RADIUS * Math.sqrt(3.0));
			y = y - (i - 1) * Ball.RADIUS;
			for (int j = 0; j < i; j++)
			{
				Color color = new Color(randomGenerator.nextInt(255), randomGenerator.nextInt(255), randomGenerator.nextInt(255));  //Color randomizer
				balls.add(new Ball(x,y,color));
				y = y + (2*Ball.RADIUS);
			}
		
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(Billiard::new);
	}
}