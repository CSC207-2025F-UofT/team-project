package view;

import javax.swing.*;
import java.awt.*;


public class WatchlistView extends JPanel {
    public final String viewName = "WATCHLIST";
    private JLabel titleLabel;

    public WatchlistView() {
        this.setLayout(new BorderLayout());

        titleLabel = new JLabel("My Watchlist (UC 7)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        this.add(titleLabel, BorderLayout.NORTH);

        JButton switchButton = new JButton("Go to Favorites View");
        this.add(switchButton, BorderLayout.SOUTH);
    }

    public String getViewName() {
        return viewName;
    }
}
