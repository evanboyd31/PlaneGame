package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements Runnable, MouseListener, MouseMotionListener, KeyListener, FocusListener {
    JFrame frame;
    public static void main(String[] args){
        new Game();
    }

    public Game() {
        // setup jpanel
        this.setBackground(Color.blue);
        this.setPreferredSize(new Dimension(800, 600));
        this.setLayout(null);
        initLabels();


        // setup frame
        frame = new JFrame("Plane Game");
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(false);
        frame.pack();

        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle("Plane Game");


        // add listeners
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);



        // run
        this.run();
    }
    public void initLabels(){
        JLabel mainlbl = new JLabel("PLANE GAME");
        mainlbl.setForeground(Color.WHITE);
        mainlbl.setFont(new Font("Serif", Font.BOLD, 36));
        mainlbl.setBounds(275, 100, 300, 300);
        mainlbl.setVisible(true);
        add(mainlbl);

        JLabel lbl2 = new JLabel("Press any key to begin");
        lbl2.setForeground(Color.WHITE);
        lbl2.setFont(new Font("Serif", Font.BOLD, 20));
        lbl2.setBounds(300, 137, 300, 300);
        lbl2.setVisible(true);
        add(lbl2);
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
