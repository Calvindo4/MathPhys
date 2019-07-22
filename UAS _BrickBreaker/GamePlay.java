import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.Timer;

import javax.swing.JPanel;

public class GamePlay extends JPanel implements KeyListener, ActionListener{
	Random randomGenerator = new Random();
	private boolean play = false;
	//define score
	private double score = 0;

	//define key hold
	private double hold = 0;
	
	//define total amount of bricks
	private double totalBricks = 28;
	
	//define game time flow
	private Timer timer;
	private double delay = 3;
	
	//define paddle initial location
	private double playerX = 310;
	
	//define ball properties
	private double ballposX = 350;
	private double ballposY = 530;
	private double ballXdir = 1 + randomGenerator.nextInt(2);
	private double ballYdir = 1 + randomGenerator.nextInt(1);

	//define powerup properties
	private double powerPosX = (120 + (77 * (1 + randomGenerator.nextInt(6))));
	private double powerPosY = (70 + (50 * (1 + randomGenerator.nextInt(3))));
	private double powerUp = 1;
	private double powerHit = 0;
	private double powerTime = 0;

	//define map from MapGenerator
	private MapGenerator map;
	
	public GamePlay() {
		map = new MapGenerator(4, 7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer((int)delay, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		//draw background
		g.setColor(Color.BLACK);
		g.fillRect(1, 1,  692, 592);
		
		//draw map
		map.draw((Graphics2D)g);
		
		//draw borders
		g.setColor(Color.CYAN);
		g.fillRect(0,  0,  3,  590);
		g.fillRect(0,  0,  692,  3);
		g.fillRect(691,  0,  3,  590);
		
		//draw score
		g.setColor(Color.WHITE);
		g.setFont(new Font("consolas", Font.BOLD, 30));
		g.drawString("" + score, 590, 30);
		
		//draw paddle
		g.setColor(Color.GREEN);
		g.fillRect((int)playerX, 550, 100, 20);
		//if paddle gets a powerup
		if(powerUp == 0){
			g.fillRect((int)playerX, 550, 150, 20);
		}
		//if powerup time is over
		else if(powerTime > 100000) {
			g.fillRect((int)playerX, 550, 100, 20);
		}
		
		//draw ball
		g.setColor(Color.YELLOW);
		g.fillOval((int)ballposX, (int)ballposY, 20, 20);
		
		//draw powerup
		g.fillOval((int)powerPosX, (int)powerPosY, 0, 0);
		//if ball hits a brick with a powerup inside
		if(powerHit >= 1) {
			g.setColor(Color.WHITE);
			g.fillOval((int)powerPosX, (int)powerPosY, 15, 15);
		}
		//if powerup is gotten by paddle
		else if(powerUp == 0) {
			g.fillOval((int)powerPosX, (int)powerPosY, 0, 0);
		}

		//game over condition if all bricks are destroyed
		if(totalBricks <= 0) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.GREEN);
			g.setFont(new Font("consolas", Font.BOLD, 30));
			g.drawString("You Win!!!", 260, 300);
		
			g.setFont(new Font("consolas", Font.BOLD, 20));
			g.drawString("Press Enter to restart the game", 170, 350);
		}
		
		//game over condition if paddle miss the ball
		if(ballposY > 570) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("consolas", Font.BOLD, 30));
			g.drawString("Game Over....", 240, 300);
		
			g.setFont(new Font("consolas", Font.BOLD, 20));
			g.drawString("Press Enter to restart the game", 170, 350);
		}
		
		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		Rectangle ballRect = new Rectangle((int)ballposX, (int)ballposY, 20, 20);
		Rectangle powerRect = new Rectangle((int)powerPosX, (int)powerPosY, 79, 52);
		Rectangle playerRect = new Rectangle((int)playerX, 550, 110, 20);
		//condition if powerup is hit by paddle
		if (powerUp == 0) {
			playerRect = new Rectangle ((int)playerX, 550, 160, 20);
			powerTime++;
			//condition if powerup time is over
			if(powerTime > 100000) {
				playerRect = new Rectangle ((int)playerX, 550, 110, 20);
			}
			//condition if powerup time is not over
			else if(powerTime < 100000) {
				powerTime ++;
			}
		}
		if(play) {
			//condition if paddle hits ball
			if(ballRect.intersects(playerRect)) {
				ballYdir = -ballYdir;
			}
			
			//brick generator from MapGenerator
		  	A:for(int i = 0; i < map.map.length; i++) {
				for(int j = 0; j < map.map[0].length; j++) {
					if(map.map[i][j] > 0) {
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;

						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle brickRect = rect;
						
						//condition if ball hits a brick
						if(ballRect.intersects(brickRect)) {
							map.setBrickValue(0, i, j);
							totalBricks--;
							score += 5;
							
							if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
								ballXdir = -ballXdir;
							}
							else {
								ballYdir = -ballYdir;
							}
							
							break A;
						}

						//condition if ball hits a brick with a powerup inside
						if(ballRect.intersects(powerRect)) {
							powerHit = 1;
						}
						if(powerHit >= 1) {
							powerRect = new Rectangle((int)powerPosX, (int)powerPosY, 15, 15);
							powerPosY += 0.06;
						}
						if(powerUp == 0) {
							powerPosX = -20;
							powerPosY = -20;
						}

						//condition if paddle hits a powerup
						if(playerRect.intersects(powerRect)) {
							powerUp = 0;
							powerHit = 0;
							score += 10;
							powerPosX = -20;
							powerPosY = -20;
						}
					}
				}
			}
			
			ballposX += ballXdir;
			ballposY += ballYdir;
			if(ballposX < 0) {
				ballXdir = -ballXdir;
			}
			if(ballposY < 0) {
				ballYdir = -ballYdir;
			}
			if(ballposX > 670) {
				ballXdir = -ballXdir;
			}
		}
		repaint();
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		//condition if player press right arrow key
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(playerX >= 635) {
				playerX = 635;
				hold++;
				if(hold >= 1) {
					playerX++;
					moveRight();
				}
			}
			else {
				moveRight();
				hold = 0;
			}
		}
		//condition if player press left arrow key
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(playerX < 5) {
				playerX = 5;
				hold++;
				if(hold >= 1) {
					playerX--;
					moveLeft();
				}
			}
			else {
				moveLeft();
				hold = 0;
			}
		}
		//condition if player press enter after game over
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(!play) {
				play = true;
				ballposX = 350;
				ballposY = 530;
				ballXdir = 1 + randomGenerator.nextInt(2);
				ballYdir = 1 + randomGenerator.nextInt(1);
				powerPosX = (120 + (77 * (1 + randomGenerator.nextInt(6))));
				powerPosY = (70 + (50 * (1 + randomGenerator.nextInt(3))));
				playerX = 310;
				score = 0;
				totalBricks = 28;
				powerUp = 1;
				powerHit = 0;
				powerTime = 0;
				map = new MapGenerator(4, 7);
				
				repaint();
			}
		}
	}

	public void moveRight() {
		play = true;
		playerX += 50;
	}
	public void moveLeft() {
		play = true;
		playerX -= 50;
	}
}
