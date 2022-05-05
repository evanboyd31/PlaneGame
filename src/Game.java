package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements Runnable, MouseListener, MouseMotionListener, KeyListener, FocusListener {
    double startTime;
    JFrame frame;
    public static void main(String[] args){
        new Game();
    }

    public Game() {
        // setup jpanel
        this.setBackground(Color.blue);
        this.setPreferredSize(new Dimension(800, 600));

        // setup frame
        frame = new JFrame("Plane Game");
        frame.add(this, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(false);
        frame.pack();
        frame.setVisible(true);
        frame.setTitle("Plane Game");

        // add listeners
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);

        // init start time
        this.startTime = (System.nanoTime() / 1_000_000_000.0);


        // run
        this.run();
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void run() {

    }
}
