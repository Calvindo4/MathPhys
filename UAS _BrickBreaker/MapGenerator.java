import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class MapGenerator {
	Random randomGenerator = new Random();

	public int map[][];
	public int brickWidth;
	public int brickHeight;
	public MapGenerator(int row, int col) {
		map = new int[row][col];
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				map[i][j] = 1;
			}
		}
		
		brickWidth = 540/col;
		brickHeight = 200/row;
	}

	//draw background and bricks
	public void draw(Graphics2D g) {
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				if(map[i][j] > 0) {
					Color color = new Color(randomGenerator.nextInt(255),randomGenerator.nextInt(255),randomGenerator.nextInt(255));
					//SWAP CODE COMMENT AT YOUR OWN RISK
					g.setColor(Color.MAGENTA);
					//g.setColor(color);
					g.fillRect(j * brickWidth + 88, i * brickHeight + 50, brickWidth, brickHeight);
					
					g.setStroke(new BasicStroke());
					g.setColor(Color.BLACK);
					g.drawRect(j * brickWidth + 88, i * brickHeight + 50, brickWidth, brickHeight);
				}
			}
		}
	}
	public void setBrickValue(int value, int row, int col) {
		map[row][col] = value;
	}
}
