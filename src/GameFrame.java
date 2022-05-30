package src;

import javax.swing.*;

/**
 * The GameFrame class extends JFrame and contains the GamePanel
 * on which the plane game is simulated
 *
 * @author evanb
 */
public class GameFrame extends JFrame {
    /**
     * Constructor sets properties of the GameFrame instance,
     * and creates a new GamePanel on which the game is simulated
     */
    GameFrame(){
        this.add(new GamePanel());
        this.setTitle("Plane game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
