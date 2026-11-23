package app;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import view.GameView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Stressdew Valley");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setContentPane(new GameView());
            window.setSize(800, 550);
            window.setMinimumSize(new Dimension(720, 480));
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}
