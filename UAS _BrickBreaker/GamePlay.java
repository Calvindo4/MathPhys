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
	private int score = 0;
	
	private int totalBricks = 28;
	private int powerUp = 1;
	private int powerTime = 0;
	
	private Timer timer;
	private int delay = 3;
	
	private int playerX = 310;
	
	private int ballposX = 350;
	private int ballposY = 530;
	private int ballXdir = 1 + randomGenerator.nextInt(2);
	private int ballYdir = 1 + randomGenerator.nextInt(1);

	private int powerPosX = (540/8 + 50) * (1 + randomGenerator.nextInt(2));
	private int powerPosY = (200/4 + 50) * (1 + randomGenerator.nextInt(2));

	private MapGenerator map;
	
	public GamePlay() {
		map = new MapGenerator(4, 7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		//background
		g.setColor(Color.BLACK);
		g.fillRect(1, 1,  692, 592);
		
		//map
		map.draw((Graphics2D)g);
		
		//borders
		g.setColor(Color.CYAN);
		g.fillRect(0,  0,  3,  590);
		g.fillRect(0,  0,  692,  3);
		g.fillRect(691,  0,  3,  590);
		
		//scores
		g.setColor(Color.WHITE);
		g.setFont(new Font("consolas", Font.BOLD, 30));
		g.drawString("" + score, 590, 30);
		
		//paddle
		g.setColor(Color.GREEN);
		g.fillRect(playerX, 550, 100, 20);
		if(powerUp == 0){
			g.fillRect(playerX, 550, 150, 20);
		}
		else if(powerTime > 100000) {
			g.fillRect(playerX, 550, 100, 20);
		}
		
		//ball
		g.setColor(Color.YELLOW);
		g.fillOval(ballposX, ballposY, 20, 20);
		
		//powerup
		g.fillOval(powerPosX, powerPosY, 0, 0);
		if(score >= 50) {
			g.setColor(Color.MAGENTA);
			g.fillOval(powerPosX, powerPosY, 15, 15);
		}
		else if(powerUp == 0) {
			g.fillOval(powerPosX, powerPosY, 0, 0);
		}

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
		Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
		Rectangle powerRect = new Rectangle(powerPosX, powerPosY, 0, 0);
		Rectangle playerRect = new Rectangle(playerX, 550, 110, 20);
		if (powerUp == 0) {
			playerRect = new Rectangle (playerX, 550, 160, 20);
			powerTime++;
			if(powerTime < 100000) {
				powerTime ++;
			}
			else if(powerTime <= 100000) {
				powerTime ++;
			}
			else if(powerTime > 100000) {
				playerRect = new Rectangle (playerX, 550, 110, 20);
			}
		}
		if(play) {
			if(ballRect.intersects(playerRect)) {
				ballYdir = -ballYdir;
			}
			
		  	A:for(int i = 0; i < map.map.length; i++) {
				for(int j = 0; j < map.map[0].length; j++) {
					if(map.map[i][j] > 0) {
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;

						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle brickRect = rect;

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

						if(score >= 100) {
						powerRect = new Rectangle(powerPosX, powerPosY, 15, 15);
							if(powerPosY < 650) {
								powerPosY ++;
							}
							else if(powerPosY <= 650) {
								powerPosY ++;
							}
						}

						if(playerRect.intersects(powerRect)) {
							powerUp = 0;
							score += 10;
							powerPosX = -10;
							powerPosY = -10;
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
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(playerX >= 635) {
				playerX = 635;
			}
			else {
				moveRight();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(playerX < 5) {
				playerX = 5;
			}
			else {
				moveLeft();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(!play) {
				play = true;
				ballposX = 350;
				ballposY = 530;
				ballXdir = 1 + randomGenerator.nextInt(2);
				ballYdir = 1 + randomGenerator.nextInt(1);
				powerPosX = (540/8 + 50) * (1 + randomGenerator.nextInt(2));
				powerPosY = (200/4 + 50) * (1 + randomGenerator.nextInt(2));
				playerX = 310;
				score = 0;
				totalBricks = 28;
				powerUp = 1;
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
