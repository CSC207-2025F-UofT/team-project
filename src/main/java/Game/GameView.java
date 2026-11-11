package Game;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GameView extends JFrame {

    public GameView() {
        setTitle("Five in a Row");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel board = new JPanel(new GridLayout(15, 15));



        for (int i = 0; i < 225; i++) {
            JButton button = new JButton();
            final int x = i;
            button.addActionListener(e -> {
                System.out.println("Button"+ x +" was clicked");
            });
            button.setBackground(new Color(255, 228, 163));
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorder(new LineBorder(Color.GRAY, 1));

            board.add(button);
        }
        board.setPreferredSize(new Dimension(800, 800));

        JPanel container = new JPanel(new GridBagLayout());

        container.add(board);

        add(container);

        pack();
        setSize(1000,1000);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameView());
    }
}
