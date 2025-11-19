package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class WatchlistView extends JPanel {
    public final String viewName = "WATCHLIST";
    private JLabel titleLabel;
    private JButton switchButton;

    public WatchlistView() {
        this.setLayout(new BorderLayout());

        titleLabel = new JLabel("My Watchlist", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        this.add(titleLabel, BorderLayout.NORTH);

        switchButton = new JButton("Go to Favorites View");
        this.add(switchButton, BorderLayout.SOUTH);
    }

    public void setswitchtofavButtonListener(ActionListener listener) {
        switchButton.addActionListener(listener);
    }

    public String getViewName() {
        return viewName;
    }
}
