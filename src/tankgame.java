
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author vandl4973
 */
public class tankgame extends JComponent implements KeyListener {

    // Height and Width of our game
    static final int WIDTH = 800;
    static final int HEIGHT = 300;
    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;
    // game vairbles
    // adding speed of the boulders
    int speed = 5;
    Color dirt = new Color(171, 99, 5);
    Color tankcolour = new Color(63, 204, 89);
    // tank varibles
    Rectangle tank = new Rectangle(100, 300, 40, 30);
    //bolder varibles
    Rectangle[] boulder = new Rectangle[6];
    // making the boulder vairbles
    //height of the boulder
    int boulderHeight = 100;
    // width of the boulder
    int boulderWidth = 100;
    //minimum distance
    int minDistance = 75;
    // boulder spacing
    int boulderSpacing = 250;
    //adding death vairable
    boolean dead = false;
   
    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // GAME DRAWING GOES HERE 
        //changing the color the sky 
        g.setColor(dirt);
        //making the backround
        g.fillRect(0, 0, WIDTH, HEIGHT);
        // making a road
        g.setColor(Color.BLACK);
        g.fillRect(0, 100, WIDTH, 100);
        //making the tank
        g.setColor(tankcolour);
        g.fillRect(tank.x, tank.y, tank.width, tank.height);
        //making the barrel of the tank
        g.setColor(tankcolour);
        g.fillRect(tank.x + 40, tank.y + 10, tank.width - 20, tank.height - 20);
        //adding boulders to dodge
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < boulder.length; i++) {
            g.fillRect(boulder[i].x, boulder[i].y, boulder[i].width, boulder[i].height);
        }
    }
    // GAME DRAWING ENDS HERE

    public void setBoulder(int boulderPosition) {
        //randomnumber generator
        Random randGen = new Random();
        // generate a random y postion

        int boulderY = randGen.nextInt(HEIGHT - 2 * minDistance) + minDistance;
        //generate the new boulderX cooridinate
        int boulderX = boulder[boulderPosition].x;
        boulderX = boulderX + (boulderWidth + boulderSpacing) * boulder.length;
        boulder[boulderPosition].setBounds(boulderX, boulderY, boulderWidth, boulderHeight);

    }
    public void reset(){
         //set up boulders
        int boulderX = 700;
        Random randGen = new Random();
        for (int i = 0; i < boulder.length; i++) {
            //generating a random y postion
            int boulderY = randGen.nextInt(HEIGHT - 2 * minDistance) + minDistance;
            boulder[i] = new Rectangle(boulderX, boulderY, boulderWidth, boulderHeight);
           
            //move the boulderX value over
            boulderX = boulderX + boulderWidth +boulderSpacing;
            
        }
        //reset the tank
        tank.y=100;
        dead=false;
    }
    // The main game loop
    // In here is where all the logic for my game will go

    public void run() {
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;

        //set up boulder
         int boulderX = 700;
        Random randGen = new Random();
        for (int i = 0; i < boulder.length; i++) {
            //generating a random y postion
            int boulderY = randGen.nextInt(HEIGHT - 2 * minDistance) + minDistance;
            boulder[i] = new Rectangle(boulderX, boulderY - boulderHeight, boulderWidth, boulderHeight);
            //move the Pipe X value over
            boulderX = boulderX + boulderWidth + boulderSpacing;

        }


        // the main game loop section
        // game will end if you set done = false;
        boolean done = false;
        while (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 
            // making barrers for map 
            if (tank.y < 0) {
                tank.y = 0;
            } else if (tank.y + tank.height > HEIGHT) {
                tank.y = HEIGHT - tank.height;
            }
            //if the tank hits the boulders
                for(int i =0; i<boulder.length;i++){
                    if(tank.intersects(boulder[i])){
                        dead = true;
                }
                }
            for (int i = 0; i < boulder.length; i++) {
                boulder[i].x = boulder[i].x - speed;
                //check if a boulder went off the screen
                if (boulder[i].x + boulderWidth < 0) {
                    //move the boulder
                    setBoulder(i);
                }
            }

            // GAME LOGIC ENDS HERE 

            // update the drawing (calls paintComponent)
            repaint();



            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            try {
                if (deltaTime > desiredTime) {
                    //took too much time, don't wait
                    Thread.sleep(1);
                } else {
                    // sleep to make up the extra time
                    Thread.sleep(desiredTime - deltaTime);
                }
            } catch (Exception e) {
            };
        }
    }



        /**
         * @param args the command line arguments
         */
    

    public static void main(String[] args) {
        // creates a windows to show my game
        JFrame frame = new JFrame("My Game");

        // creates an instance of my game
        tankgame game = new tankgame();
        // sets the size of my game
        game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(game);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);
        frame.addKeyListener(game);
        // starts my game loop
        game.run();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) {
            tank.y = tank.y - 35;
        }
        if (key == KeyEvent.VK_DOWN) {
            tank.y = tank.y + 35;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}