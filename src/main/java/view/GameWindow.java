package view;

import javax.swing.JFrame;

/** The application window. Only holds the game panel. */
public class GameWindow extends JFrame {
    public GameWindow(GamePanel panel) {
        super("SpellBook");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(panel);
        pack();
        setLocationRelativeTo(null);
    }
}
