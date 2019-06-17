//package Meeting03_Dribble;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.*;

public class Ball {
    private double positionX;                   // center of ball's position
    private double positionY;
    private double radius;
    private double velocityX;                   // ball's velocity
    private double velocityY;
    private Color ballColor;
    private double e = 0.9;        // ball's coefficient of resistution
    private final static double a = 0.005;
    private final static double GRAVITY = 0;  // use custom gravity

    public void ChangeCor(double corValue)
    {
        e = corValue;
        return;
    }

    public Ball(double positionX, double positionY, double radius, double velocityX, double velocityY, double coefficient, Color ballColor) {
        this.radius = radius;
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.e = coefficient;   //Every ball now has their own COR
        this.ballColor = ballColor;
    }

    // drawing function
    public void draw(Graphics g) {
        Color tempColor = g.getColor();
        g.setColor(ballColor);
        g.fillOval((int) (positionX - radius), (int) (positionY - radius), (int) (2 * radius), (int) (2 * radius));
        g.setColor(tempColor);
    }

    // move the ball by modifying current position, with assumption that time = 1
    public void move() {
        positionX += velocityX;
        positionY -= (velocityY + GRAVITY/2);
        double velocity = Math.sqrt(velocityX * velocityX + velocityY * velocityY);

        if (velocity > 0){
            velocityX -= a;
            velocityY -= a;
        }
    }

    // check collision between walls and the ball
    public void detectCollision(ArrayList<Wall> walls) {
        for (Wall w : walls) {
            if(w.distanceFromPoint(positionX, positionY) <= radius) {
                double error = radius - w.distanceFromPoint(positionX, positionY);

                positionX += error * w.normalLine().getX();
                positionY -= error * w.normalLine().getY();

                //if the ball collided with a vertical wall
                if(w.getWidth() == 0) {
                    velocityX = -e * velocityX;
                }

                //if the ball collided with a horizontal wall
                else if(w.getHeight() == 0) {
                    velocityY = -e * velocityY;
                }
            }
        }
    }
}