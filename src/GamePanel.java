package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int DELAY = 75;
    int playerX = SCREEN_WIDTH / 2 - 32;
    int playerY = SCREEN_HEIGHT - 64;
    int enemyX = SCREEN_WIDTH / 2 - 32;
    int enemyY = -100;
    int bulletX = playerX + 16;
    int bulletY = playerY + 5;
    int cloudX = SCREEN_WIDTH / 2 - 32;
    int cloudY = -400;
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


    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(66, 135, 245));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
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
            cloudY += 1;
            cloudLabel.setLocation(cloudX, cloudY);

            //this.add(picLabel, x[0], y[0]);
            planeLabel.setLocation(playerX, playerY);
            if (bulletFiring) {
                bulletY -= 2.5;
            }
            if (bulletY < 0) {
                bulletFiring = false;
                bulletX = playerX + 16;
                bulletY = playerY + 5;
            }
            bulletLabel.setLocation(bulletX, bulletY);
            enemyY += 1;
            enemyLabel.setLocation(enemyX, enemyY);
            this.revalidate();
            this.repaint();

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + score, (SCREEN_WIDTH - metrics.stringWidth("Score: " + score)) / 2, g.getFont().getSize());
        } else {
            if (!playerWelcomed) {
                welcomeScreen(g);
            } else {
                gameOver(g);
            }
        }

    }

    public void newCloud() {
        cloudX = random.nextInt(0, 636);
        cloudY = random.nextInt(-300, -50);
    }

    public void move() {
        switch (direction) {
            case 'U':
                playerY -= UNIT_SIZE;
                if (!bulletFiring) {
                    bulletY -= UNIT_SIZE;
                }
                break;
            case 'D':
                playerY += UNIT_SIZE;
                if (!bulletFiring) {
                    bulletY += UNIT_SIZE;
                }
                break;
            case 'L':
                playerX -= UNIT_SIZE;
                if (!bulletFiring) {
                    bulletX -= UNIT_SIZE;
                }
                break;
            case 'R':
                playerX += UNIT_SIZE;
                if (!bulletFiring) {
                    bulletX += UNIT_SIZE;
                }
                break;
            case ' ':
                break;
        }
    }


    public void checkCollisions() {


        // check if head touches left border
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
                bulletX = playerX + 16;
            }

        }

        // top border
        if (playerY < 0) {
            playerY = 0;
            if (!bulletFiring) {
                bulletY = playerY + 5;
            }
        }

        // bottom border
        if (playerY > SCREEN_HEIGHT - 64) {
            playerY = SCREEN_HEIGHT - 64;
            if (!bulletFiring) {
                bulletY = playerY + 5;
            }
        }


        double distance = Math.sqrt((Math.pow((enemyX - bulletX), 2)) + (Math.pow((enemyY - bulletY), 2)));
        if (distance < 64 && bulletFiring) {
            enemyX = random.nextInt(SCREEN_WIDTH - 64);
            enemyY = -100;
            bulletFiring = false;
            bulletX = playerX + 16;
            bulletY = playerY + 5;
            score++;
        }
        if (enemyY > SCREEN_HEIGHT - 64) {
            running = false;
        }

        if (cloudY > SCREEN_HEIGHT - 64) {
            newCloud();
        }

        if (!running) {
            timer.stop();
        }

    }

    public void clear() {
        this.remove(planeLabel);
        this.remove(enemyLabel);
        this.remove(bulletLabel);
        this.remove(cloudLabel);
    }

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

    public void gameOver(Graphics g) throws IOException {
        //Game over text
        clear();
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game over", (SCREEN_WIDTH - metrics1.stringWidth("Game over")) / 2, SCREEN_HEIGHT / 2);
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + score, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + score)) / 2, g.getFont().getSize());

        int highScore = getHighScore();
        if (score > highScore) {
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
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            FontMetrics metrics3 = getFontMetrics(g.getFont());
            g.drawString("High Score: " + highScore, (SCREEN_WIDTH - metrics3.stringWidth("High Score: " + highScore)) / 2, SCREEN_HEIGHT / 2 + g.getFont().getSize());
        }


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

    public int getHighScore() {
        String line = null;
        File file = new File("src/highScores.txt");
        int highScore = 0;

        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exists");
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
                    } else {
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

