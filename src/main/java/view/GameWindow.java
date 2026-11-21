package view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/** Displays the game window or an explanation when startup cannot finish. */
public class GameWindow extends JFrame {
    public GameWindow(GamePanel panel) {
        super("SpellBook");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(panel);
        pack();
        setLocationRelativeTo(null);
    }

    /** Shows a startup failure before a game window or loop is opened. Runs on Swing's event thread. */
    public static void showStartupError(String message) {
        JOptionPane.showMessageDialog(null, message, "Cannot start SpellBook", JOptionPane.ERROR_MESSAGE);
    }
}
