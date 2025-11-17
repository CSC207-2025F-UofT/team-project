package view;

import javax.swing.*;
import java.awt.*;

public class FavoritesView extends JPanel {
    public final String viewName = "FAVORITES";
    private JLabel titleLabel;

    public FavoritesView() {
        this.setLayout(new BorderLayout());

        titleLabel = new JLabel("My Favorites (UC 8)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        this.add(titleLabel, BorderLayout.NORTH);

        JButton switchButton = new JButton("Go to Watchlist View");
        this.add(switchButton, BorderLayout.SOUTH);
    }

    public String getViewName() {
        return viewName;
    }
}