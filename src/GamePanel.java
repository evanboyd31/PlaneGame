package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

/**
 * GamePanel handles the player movement, enemy movement, firing of bullets,
 * welcome and game over screens
 *
 * @author evanb
 */
public class GamePanel extends JPanel implements ActionListener {
    // instance variables
    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 600;
    static final int DELAY = 75;
    int playerX = SCREEN_WIDTH / 2 - 32;
    int playerY = SCREEN_HEIGHT - 64;
    final int playerSpeed = 25;
    int enemyX = SCREEN_WIDTH / 2 - 32;
    int enemyY = -100;
    final int enemySpeed = 1;
    int bulletX = playerX + 16;
    int bulletY = playerY + 5;
    final double bulletSpeed = 2.5;
    int cloudX = SCREEN_WIDTH / 2 - 32;
    int cloudY = -400;
    final int cloudSpeed = 1;
    char direction = ' '; //U, D, L, R
    boolean running = false;
    boolean playerWelcomed = false;
    boolean bulletFiring = false;
    int score = 0;
    Timer timer;
    Random random;
    JLabel planeLabel;
    JLabel bulletLabel;
    JLabel enemyLabel;
    JLabel cloudLabel;

    /**
     * GamePanel constructor. Basic setup of the panel takes place here.
     */
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(66, 135, 245));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
    }

    /**
     * The start game method creates a timer and starts it.
     */
    public void startGame() {
        timer = new Timer(DELAY, this);
        timer.start();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            draw(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The draw method adjusts player, enemy, bullet, and other positions while the
     * running method is true. If the game is not running, then the welcome screen or
     * game over screens are displayed
     *
     * @param g
     * @throws IOException
     */
    public void draw(Graphics g) throws IOException {
        // check to see if the game is running
        if (running) {
            // update cloud position
            cloudY += cloudSpeed;
            cloudLabel.setLocation(cloudX, cloudY);

            // update player location based on movement handled by move method
            planeLabel.setLocation(playerX, playerY);

            // check to see if the bullet is currently being fired
            if (bulletFiring) {
                bulletY -= bulletSpeed;
            }

            // check to see if the bullet is off the screen. If so, reset its location to be the same as the player
            if (bulletY < 0) {
                bulletFiring = false;
                bulletX = playerX + 16;
                bulletY = playerY + 5;
            }
            bulletLabel.setLocation(bulletX, bulletY);

            // update enemy location
            enemyY += enemySpeed;
            enemyLabel.setLocation(enemyX, enemyY);

            // revalidate and repaint to reflect changes
            this.revalidate();
            this.repaint();

            // text to display the current score
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + score, (SCREEN_WIDTH - metrics.stringWidth("Score: " + score)) / 2, g.getFont().getSize());
        } else {
            // check to see if the welcome screen has already been shown
            if (!playerWelcomed) {
                // player has not been welcomed, display welcome screen
                welcomeScreen(g);
            } else {
                // player has already been welcomed, and the game is not running. So the game is over
                gameOver(g);
            }
        }
    }

    /**
     * Generates a new random location for the cloud image once the cloud has
     * past the bottom boundary.
     */
    public void newCloud() {
        cloudX = random.nextInt(0, 636);
        cloudY = random.nextInt(-300, -50);
    }

    /**
     * The move method handles the movement of the player based on
     * the direction variable.
     */
    public void move() {
        switch (direction) {
            case 'U':
                // move player up
                playerY -= playerSpeed;
                if (!bulletFiring) {
                    // the bullet is not being fired, move the bullet with the player
                    bulletY -= playerSpeed;
                }
                break;
            case 'D':
                // move player down
                playerY += playerSpeed;
                if (!bulletFiring) {
                    // the bullet is not being fired, move the bullet with the player
                    bulletY += playerSpeed;
                }
                break;
            case 'L':
                // make player left
                playerX -= playerSpeed;
                if (!bulletFiring) {
                    // the bullet is not being fired, move the bullet with the player
                    bulletX -= playerSpeed;
                }
                break;
            case 'R':
                // move player right
                playerX += playerSpeed;
                if (!bulletFiring) {
                    // the bullet is not being fired, move the bullet with the player
                    bulletX += playerSpeed;
                }
                break;
            case ' ':
                // stop the player
                break;
        }
    }

    /**
     * The checkCollisions method checks for player impacting the border of the screen,
     * the enemy/cloud reaching bottom of screen, and bullets impacting the enemy
     */
    public void checkCollisions() {
        // check if player touches left border
        if (playerX < 0) {
            playerX = 0;
            if (!bulletFiring) {
                bulletX = playerX + 16;
            }
        }

        // right border
        if (playerX > SCREEN_WIDTH - 64) {
            playerX = SCREEN_WIDTH - 64;
            if (!bulletFiring) {
                // set the bullet to follow the player when not firing
                bulletX = playerX + 16;
            }

        }

        // top border
        if (playerY < 0) {
            playerY = 0;
            if (!bulletFiring) {
                // set the bullet to follow the player when not firing
                bulletY = playerY + 5;
            }
        }

        // bottom border
        if (playerY > SCREEN_HEIGHT - 64) {
            playerY = SCREEN_HEIGHT - 64;
            if (!bulletFiring) {
                // set the bullet to follow the player when not firing
                bulletY = playerY + 5;
            }
        }

        // calculate the distance between the bullet and the enemy
        double distance = Math.sqrt((Math.pow((enemyX - bulletX), 2)) + (Math.pow((enemyY - bulletY), 2)));

        // check for impact
        if (distance < 64 && bulletFiring) {
            // generate new location for enemy, reset bullet location, increment score
            enemyX = random.nextInt(SCREEN_WIDTH - 64);
            enemyY = -100;
            bulletFiring = false;
            bulletX = playerX + 16;
            bulletY = playerY + 5;
            score++;
        }

        if (enemyY > SCREEN_HEIGHT - 64) {
            // enemy has reached the bottom of the screen, the game is now over
            running = false;
        }

        if (cloudY > SCREEN_HEIGHT) {
            // cloud has reached bottom, generate new location
            newCloud();
        }

        if (!running) {
            // game is over, stop the timer
            timer.stop();
        }

    }

    /**
     * Clear the screen of all the images.
     */
    public void clear() {
        this.remove(planeLabel);
        this.remove(enemyLabel);
        this.remove(bulletLabel);
        this.remove(cloudLabel);
    }

    /**
     * Resets the locations of everything, and the direction, firing, and score variables
     */
    public void reset() {
        playerX = SCREEN_WIDTH / 2 - 32;
        playerY = SCREEN_HEIGHT - 64;
        enemyX = SCREEN_WIDTH / 2 - 32;
        enemyY = -100;
        bulletX = playerX + 16;
        bulletY = playerY + 5;
        cloudX = SCREEN_WIDTH / 2 - 32;
        cloudY = -400;
        direction = ' '; //U, D, L, R
        bulletFiring = false;
        score = 0;
    }

    /**
     * Displays the game over screen
     * @param g
     * @throws IOException
     */
    public void gameOver(Graphics g) throws IOException {
        // clear all images off the screen
        clear();
        //Game over text
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game over", (SCREEN_WIDTH - metrics1.stringWidth("Game over")) / 2, SCREEN_HEIGHT / 2);
        // keep the current score on the screen
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + score, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + score)) / 2, g.getFont().getSize());

        // get high score from the file
        int highScore = getHighScore();
        if (score > highScore) {
            // there is now a new high score. Display new high score message along with high score
            addNewHighScore(score);
            g.setColor(Color.green);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            FontMetrics metrics3 = getFontMetrics(g.getFont());
            g.drawString("New High Score!", (SCREEN_WIDTH - metrics3.stringWidth("New High Score!")) / 2, SCREEN_HEIGHT / 2 + g.getFont().getSize());

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            FontMetrics metrics4 = getFontMetrics(g.getFont());
            g.drawString("High Score: " + score, (SCREEN_WIDTH - metrics4.stringWidth("High Score: " + score)) / 2, SCREEN_HEIGHT / 2 + g.getFont().getSize() + g.getFont().getSize());
        } else {
            // no new high score, display high score
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            FontMetrics metrics3 = getFontMetrics(g.getFont());
            g.drawString("High Score: " + highScore, (SCREEN_WIDTH - metrics3.stringWidth("High Score: " + highScore)) / 2, SCREEN_HEIGHT / 2 + g.getFont().getSize());
        }


    }

    /**
     * Displays the welcome screen
     * @param g
     */
    public void welcomeScreen(Graphics g) {
        // welcome text
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Plane Game", (SCREEN_WIDTH - metrics1.stringWidth("Plane Game")) / 2, SCREEN_HEIGHT / 2);
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Press the space bar to begin", (SCREEN_WIDTH - metrics2.stringWidth("Press the space bar to begin")) / 2, SCREEN_HEIGHT / 2 + 35);
    }

    /**
     * Adds and assigns images to label instance variables based on fileName passed to this method.
     * @param fileName - string containing file name and location of the image to be added
     */
    public void addImage(String fileName) {
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (fileName.equals("src/aircraft.png")) {
            planeLabel = new JLabel(new ImageIcon(myPicture));
            this.add(planeLabel);
        } else if (fileName.equals("src/bullet.png")) {
            bulletLabel = new JLabel(new ImageIcon(myPicture));
            this.add(bulletLabel);
        } else if (fileName.equals("src/plane.png")) {
            enemyLabel = new JLabel(new ImageIcon(myPicture));
            this.add(enemyLabel);
        } else if (fileName.equals("src/cloud.png")) {
            cloudLabel = new JLabel(new ImageIcon(myPicture));
            this.add(cloudLabel);
        }

    }

    /**
     * Method to get the highScore recorded in the highScores.txt file
     * @return the highest value in the highScore file
     */
    public int getHighScore() {
        String line = null;
        File file = new File("src/highScores.txt");
        int highScore = 0;

        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist");
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(fr);

        try {
            while ((line = br.readLine()) != null) {
                if (Integer.parseInt(line) > highScore) {
                    highScore = Integer.parseInt(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return highScore;
    }

    /**
     * Method to write the new high score to the highScores.txt file
     * @param newScore - the new highest score to be written to the file
     * @throws IOException
     */
    public void addNewHighScore(int newScore) throws IOException {
        PrintWriter wr = new PrintWriter(new FileWriter("src/highScores.txt"));
        wr.println(newScore);
        wr.close();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollisions();
        }
        repaint();
    }

    /**
     * Inner class that extends KeyAdapter. Uses arrow keys to move the player,
     * and the space bar to toggle from welcome screen and game over screens
     */
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction == 'R') {
                        // player is moving left, we need to stop before moving right
                        direction = ' ';
                    } else {
                        // continue moving left
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction == 'L') {
                        // player is moving right, we need to stop before moving left
                        direction = ' ';
                    } else {
                        // continue moving right
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction == 'D') {
                        // player is moving down, we need to stop before moving up
                        direction = ' ';
                    } else {
                        // continue moving up
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction == 'U') {
                        // player is moving up, we need to stop before moving down
                        direction = ' ';
                    } else {
                        // continue moving down
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (running) {
                        // the game is running, so fire a bullet
                        bulletFiring = true;
                    } else {
                        // game is not running, begin a new game
                        reset();
                        running = true;
                        playerWelcomed = true;
                        startGame();
                        addImage("src/aircraft.png");
                        addImage("src/bullet.png");
                        addImage("src/plane.png");
                        addImage("src/cloud.png");
                    }
                    break;
            }
        }
    }
}

