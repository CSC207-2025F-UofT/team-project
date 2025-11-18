package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HomepageView extends JPanel {
    public final String viewName = "HOMEPAGE";
    private JLabel titleLabel;

    private JButton browseButton;
    private JButton viewButton;
    private JButton favoritesButton;

    public HomepageView() {
        this.setLayout(new BorderLayout());

        titleLabel = new JLabel("Homepage", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        browseButton = new JButton("Browse titles");
        viewButton = new JButton("View Watchlist");
        favoritesButton = new JButton("View Favorites");

        browseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        favoritesButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(browseButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(viewButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(favoritesButton);

        this.add(buttonPanel, BorderLayout.CENTER);
    }

    public void setBrowseButtonListener(ActionListener listener) {
        browseButton.addActionListener(listener);
    }

    public void setWatchlistButtonListener(ActionListener listener) {
        viewButton.addActionListener(listener);
    }

    public void setFavoritesButtonListener(ActionListener listener) {
        favoritesButton.addActionListener(listener);
    }

    public String getViewName() {
        return viewName;
    }
}
