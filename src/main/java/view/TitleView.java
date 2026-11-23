package view;

import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TitleView extends JPanel {
    public TitleView(Runnable newGame) {
        setLayout(new GridLayout(0, 1, 12, 12));
        setBorder(BorderFactory.createEmptyBorder(80, 160, 80, 160));

        JLabel title = new JLabel("Stressdew Valley", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));
        add(title);
        add(new JLabel("A little campus life on St. George Street.", SwingConstants.CENTER));

        JButton start = new JButton("New Game");
        start.addActionListener(event -> newGame.run());
        add(start);

        // Continue will be connected to the save file in UC5.
        JButton continueGame = new JButton("Continue");
        continueGame.setEnabled(false);
        add(continueGame);
    }
}
