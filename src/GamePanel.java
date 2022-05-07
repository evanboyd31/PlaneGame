package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 900;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    int playerX = 0;
    int playerY = 0;
    int bulletX = playerX + 16;
    int bulletY = playerY + 5;
    char direction = 'R'; //U, D, L, R
    boolean running = false;
    boolean playerWelcomed = false;
    boolean bulletFiring = false;
    Timer timer;
    Random random;
    JLabel planeLabel;
    JLabel bulletLabel;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(66, 135, 245));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        //startGame();
    }

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

    public void draw(Graphics g) throws IOException {
        if (running) {


            //this.add(picLabel, x[0], y[0]);
            planeLabel.setLocation(playerX, playerY);
            if(bulletFiring){
                bulletY -= 20;
            }
            if(bulletY < 0){
                bulletFiring = false;
                bulletX = playerX + 16;
                bulletY = playerY + 5;
            }
            bulletLabel.setLocation(bulletX, bulletY);
            this.revalidate();
            this.repaint();

            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            // g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        } else {
            if (!playerWelcomed) {
                welcomeScreen(g);
            } else {
                direction = 'R'; //U, D, L, R
                playerX = 0;
                playerY = 0;
                gameOver(g);
            }
        }

    }

//    public void newApple() {
//        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
//        appleY = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
//    }

    public void move() {
        switch (direction) {
            case 'U':
                playerY -= UNIT_SIZE;
                if(!bulletFiring){
                    bulletY -= UNIT_SIZE;
                }
                break;
            case 'D':
                playerY += UNIT_SIZE;
                if(!bulletFiring){
                    bulletY += UNIT_SIZE;
                }
                break;
            case 'L':
                playerX -= UNIT_SIZE;
                if(!bulletFiring){
                    bulletX -= UNIT_SIZE;
                }
                break;
            case 'R':
                playerX += UNIT_SIZE;
                if(!bulletFiring){
                    bulletX += UNIT_SIZE;
                }
                break;
            case ' ':
                break;
        }
    }

    public void checkApple() {
//        if ((x[0] == appleX) && (y[0] == appleY)) {
//            bodyParts++;
//            applesEaten++;
//            newApple();
//        }
    }

    public void checkCollisions() {


        // check if head touches left border
        if (playerX < 0) {
            playerX = 0;
        }

        // right border
        if (playerX > SCREEN_WIDTH - 64) {
            playerX = SCREEN_WIDTH - 64;
        }

        // top border
        if (playerY < 0) {
            playerY = 0;
        }

        // bottom border
        if (playerY > SCREEN_HEIGHT - 64) {
            playerY = SCREEN_HEIGHT - 64;
        }

//        hit_detection(enemyX, enemyY, bulletX, bulletY){
//            double distance = Math.sqrt((Math.pow((enemyX - bulletX), 2)) + (Math.pow((enemyY - bulletY), 2)));
//            if distance < 35:
//            return true;
//        else:
//            return false;
//        }

        if (!running) {
            timer.stop();
        }

    }

    public void gameOver(Graphics g) {
        //Game over text
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game over", (SCREEN_WIDTH - metrics1.stringWidth("Game over")) / 2, SCREEN_HEIGHT / 2);
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 25));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        //g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    }

    public void welcomeScreen(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Plane Game", (SCREEN_WIDTH - metrics1.stringWidth("Plane Game")) / 2, SCREEN_HEIGHT / 2);
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Press the space bar to begin", (SCREEN_WIDTH - metrics2.stringWidth("Press the space bar to begin")) / 2, SCREEN_HEIGHT / 2 + 35);
    }

    public void addImage(String fileName) {
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if(fileName.equals("src/aircraft.png")){
            planeLabel = new JLabel(new ImageIcon(myPicture));
            this.add(planeLabel);
        }else if(fileName.equals("src/bullet.png")){
            bulletLabel = new JLabel(new ImageIcon(myPicture));
            this.add(bulletLabel);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction == 'R') {
                        direction = ' ';
                    } else {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction == 'L') {
                        direction = ' ';
                    } else {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction == 'D') {
                        direction = ' ';
                    } else {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction == 'U') {
                        direction = ' ';
                    } else {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (running) {
                        bulletFiring = true;
                    }else{
                        running = true;
                        playerWelcomed = true;
                        startGame();
                        addImage("src/aircraft.png");
                        addImage("src/bullet.png");
                        System.out.println(running);
                    }
                    break;
            }
        }
    }
}

