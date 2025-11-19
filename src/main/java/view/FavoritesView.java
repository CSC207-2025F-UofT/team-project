package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class FavoritesView extends JPanel {
    public final String viewName = "FAVORITES";
    private JLabel titleLabel;
    private JButton switchButton;

    public FavoritesView() {
        this.setLayout(new BorderLayout());

        titleLabel = new JLabel("My Favorites", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        this.add(titleLabel, BorderLayout.NORTH);

        switchButton = new JButton("Go to Watchlist View");
        this.add(switchButton, BorderLayout.SOUTH);
    }

    public void setswitchtowatchButtonListener(ActionListener listener) {
        switchButton.addActionListener(listener);
    }

    public String getViewName() {
        return viewName;
    }
}