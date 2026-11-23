package app;

import java.awt.Dimension;
import java.nio.file.Path;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Stressdew Valley");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Path saveFile = Path.of(System.getProperty("user.home"), ".stressdew-valley", "checkpoint.json");
            window.setContentPane(new AppBuilder(saveFile).build());
            window.setSize(800, 550);
            window.setMinimumSize(new Dimension(720, 480));
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}
