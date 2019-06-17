//package Meeting03_Dribble;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.*;

/*
    MatFis pertemuan 3
    Collision between parabolically moving object against wall

    TODO:
     0. Review about elastic and inelastic collisions. What happened when you change the coefficient of resistution (COR)?
     1. Add more balls with different colors, sizes, and velocities
     2. Create UI to add new balls and delete some instances
     3. Add COR field to the UI, so user can choose between using different COR than the default or not
     4. Turn all balls into linearly moving ones (apply Newton's first law here).
     5. Create diagonal walls and modify the calculation to adjust with diagonal walls
     6. Create UI to customize the walls
 */

public class Dribble {
    private JFrame frame;
    private DrawingArea drawingArea;

    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<Double> corCount = new ArrayList<>();

    double cor = 0.7;
    double newCor;

    int customStartX;
    int customStartY;
    int customEndX;
    int customEndY;
    
    public Dribble() {
        //configure the main canvas
        frame = new JFrame("Dribbling Balls");

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        JFrame f = new JFrame("Ball creator & deleter");
        JLabel posXLabel = new JLabel("Position (X-Axis)");
        posXLabel.setBounds(10,30,130,30);
        JLabel posYLabel = new JLabel("Position (Y-Axis)");
        posYLabel.setBounds(10,70,130,30);
        JLabel radiusLabel = new JLabel("Radius");
        radiusLabel.setBounds(10,110,130,30);
        JLabel velocityXLabel = new JLabel("Velocity (X-axis)");
        velocityXLabel.setBounds(10,150,130,30);
        JLabel velocityYLabel = new JLabel("Velocity (Y-axis)");
        velocityYLabel.setBounds(10,190,130,30);
        JLabel colorSetLabel = new JLabel("Color");
        colorSetLabel.setBounds(10,230,130,30);

        f.add(posXLabel);
        f.add(posYLabel);
        f.add(radiusLabel);
        f.add(velocityXLabel);
        f.add(velocityYLabel);
        f.add(colorSetLabel);

        JTextField posX = new JTextField();
        posX.setBounds(130,30,100,30);
        JTextField posY = new JTextField();
        posY.setBounds(130,70,100,30);
        JTextField radius = new JTextField();
        radius.setBounds(130,110,100,30);
        JTextField velocityX = new JTextField();
        velocityX.setBounds(130,150,100,30);
        JTextField velocityY = new JTextField();
        velocityY.setBounds(130,190,100,30);

        JLabel corLabel = new JLabel("Coefficient of Restitution");
        corLabel.setBounds(350,30,150,30);
        f.add(corLabel);

        JLabel info = new JLabel("If empty, COR will remain at 0.9");
        info.setBounds(430,70,400,50);
        f.add(info);

        JLabel info2 = new JLabel("COR change will take effect on the next ball");
        info2.setBounds(403,90,430,50);
        f.add(info2);

        /* COR field */
        JTextField corValue = new JTextField();
        corValue.setBounds(550,30,100,30);
        f.add(corValue);

        JTextField colorSet = new JTextField();
        colorSet.setBounds(130,230,100,30);

        f.add(posX);
        f.add(posY);
        f.add(radius);
        f.add(velocityX);
        f.add(velocityY);
        f.add(colorSet);

        JButton b = new JButton("Create new ball"); //Ball creator
        b.setBounds(100,300,100,20);    //x axis, y axis, width, height

        JButton def = new JButton("Spawn default balls");
        def.setBounds(420,300,200,20);
        def.addActionListener(new DefaultSpawn());

        JButton kill = new JButton("Delete a ball");  //Ball deleter
        kill.setBounds(300,300,100,20);
        kill.addActionListener(new CustomActionListener());

        b.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println(corValue.getText().isEmpty());   //Check if the COR field is empty or not (returns true if empty)
                
                //If not empty
                if (corValue.getText().isEmpty() == false)
                {
                    newCor = Double.parseDouble(corValue.getText());    //Parse COR text field content into double
                    //If COR is different than the original (0.7) and satisfies additional constraints below
                    if (newCor != 0.7 && newCor >= 0.0 && newCor <= 100.0)
                    {
                        cor = newCor;   //Replace current COR with the new COR
                    }
                }

                double ballCounter = balls.size();
                System.out.println(ballCounter);
                Color color2 = Color.black;  //Default color
                double pX = Double.parseDouble(posX.getText());
                double pY = Double.parseDouble(posY.getText());
                double r = Double.parseDouble(radius.getText());
                double vX = Double.parseDouble(velocityX.getText());
                double vY = Double.parseDouble(velocityY.getText());
                String col = colorSet.getText();
                switch(col.toLowerCase())
                {
                    case "black":
                        color2 = Color.black;
                        break;
                    case "blue":
                        color2 = Color.blue;
                        break;
                    case "red":
                        color2 = Color.red;
                        break;
                    case "green":
                        color2 = Color.green;
                        break;
                    case "yellow":
                        color2 = Color.yellow;
                        break;
                    case "orange":
                        color2 = Color.orange;
                        break;
                    case "gray":
                        color2 = Color.gray;
                        break;
                }
                balls.add(new Ball(pX,pY,r,vX,vY,cor,color2));
            }
        });

        f.add(b);
        f.add(kill);
        f.add(def);

        f.setSize(700,400);
        f.setLayout(null);
        f.setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // create the walls
        createWalls();

        drawingArea = new DrawingArea(frame.getWidth(), frame.getHeight(), balls, walls);
        frame.add(drawingArea);
        drawingArea.start();
    }
        private void createWalls() {
            // vertical wall must be defined in clockwise direction
            // horizontal wall must be defined in counter clockwise direction
            //(startX,startY,endX,endY,color)
            
            //Create new window for wall creation
            JFrame wallNew = new JFrame("Wall creator");
    
            JLabel startXLabel = new JLabel("Starting X");
            startXLabel.setBounds(30,10,100,30);
            JTextField startX = new JTextField();
            startX.setBounds (130,10,100,30);
    
            JLabel startYLabel = new JLabel("Starting Y");
            startYLabel.setBounds(30,40,100,30);
            JTextField startY = new JTextField();
            startY.setBounds(130,40,100,30);
    
            JLabel endXLabel = new JLabel("End X");
            endXLabel.setBounds(30,70,100,30);
            JTextField endX = new JTextField();
            endX.setBounds(130,70,100,30);
    
            JLabel endYLabel = new JLabel("End Y");
            endYLabel.setBounds(30,100,100,30);
            JTextField endY = new JTextField();
            endY.setBounds(130,100,100,30);
    
            JButton defaultWall = new JButton("Default wall");
            defaultWall.setBounds(300,20,150,30);
            defaultWall.addActionListener(new DefaultListener());
    
            JButton wallKiller = new JButton("Remove wall");
            wallKiller.setBounds(300,67,150,30);
            wallKiller.addActionListener(new wallRemover());
    
            JButton newWall = new JButton("Create wall");
            newWall.setBounds(50,150,150,30);
            
            //Listener to create a new wall
            newWall.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    //Get string data from JTextField and parse them as an integer
                    customStartX = Integer.parseInt(startX.getText());
                    customStartY = Integer.parseInt(startY.getText());
                    customEndX = Integer.parseInt(endX.getText());
                    customEndY = Integer.parseInt(endY.getText());
    
                    walls.add(new Wall(customStartX,customStartY,customEndX,customEndY,Color.black));   //Create wall
                }
            });
    
            
            wallNew.add(startXLabel);
            wallNew.add(startX);
            wallNew.add(startYLabel);
            wallNew.add(startY);
            wallNew.add(endXLabel);
            wallNew.add(endX);
            wallNew.add(endYLabel);
            wallNew.add(endY);
    
            wallNew.add(defaultWall);
            wallNew.add(wallKiller);
            wallNew.add(newWall);
            wallNew.setSize(640,350);
            wallNew.setLayout(null);
            wallNew.setVisible(true);
        }
    
        //ActionListener to kill balls (corresponds to 'kill' button)
        class CustomActionListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int counter = balls.size();
                if (counter != 0)
                {
                    balls.remove(counter-1);
                }
            }
        }
    
        //Listener to spawn default walls (rectangle)
        class DefaultListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                walls.add(new Wall(1300, 100, 50, 100, Color.black));	// horizontal top
                walls.add(new Wall(50, 600, 1300, 600, Color.black));  // horizontal bottom
                walls.add(new Wall(1300, 100, 1300, 600, Color.black));  // vertical right
                walls.add(new Wall(50, 600, 50, 100, Color.black));  // vertical left
            }
        }
        
        //Listener to generate default balls
        class DefaultSpawn implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // create the ball
                balls.add(new Ball(300, 200, 50, 10, 10, cor, Color.blue));
                balls.add(new Ball(300, 100, 20, 3, 3, cor, Color.green));
                balls.add(new Ball(300, 150, 30, 7, 5, cor, Color.red));
                balls.add(new Ball(300, 250, 25, 13, 6, cor, Color.yellow));
                balls.add(new Ball(300, 400, 55, 12, 15, cor, Color.black));
            }
        }
    
        //Listener to remove a wall
        class wallRemover implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int counter = walls.size();
                if (counter != 0)
                {
                    walls.remove(counter-1);
                }
            }
        }
    
        public static void main(String[] args) {
            EventQueue.invokeLater(Dribble::new);
        }
    }