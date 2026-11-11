package Game;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {

    public GameView() {
        setTitle("Five in a Row");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel board = new JPanel(new GridLayout(15, 15));

        for (int i = 0; i < 225; i++) {
            JButton button = new JButton();
            board.add(button);
        }

        board.setPreferredSize(new Dimension(600, 600));

        add(board);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameView());
    }
}