package app;

import interface_adapter.ViewManagerModel;

import view.WatchlistView;
import view.FavoritesView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;


public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private WatchlistView watchlistView;
    private FavoritesView favoritesView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addWatchlistView() {
        watchlistView = new WatchlistView();
        cardPanel.add(watchlistView, watchlistView.getViewName());
        return this;
    }

    public AppBuilder addFavoritesView() {
        favoritesView = new FavoritesView();
        cardPanel.add(favoritesView, favoritesView.getViewName());
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Watchlist");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 1. ADD THE CARD CONTAINER TO THE FRAME FIRST
        application.add(cardPanel);

        // 2. Set preferred size and center the window
        application.pack();
        application.setSize(800, 600);
        application.setLocationRelativeTo(null);

        // 3. SCHEDULE the view switch for later (Guaranteed fix)
        SwingUtilities.invokeLater(() -> {
            // This code runs *after* the JFrame is displayed and ready.
            if (watchlistView != null) {
                viewManagerModel.setState(watchlistView.getViewName());
                viewManagerModel.firePropertyChange();
            }
        });

        // 4. Make the window visible (must be outside the invokeLater)
        application.setVisible(true);

        return application;
    }
}
