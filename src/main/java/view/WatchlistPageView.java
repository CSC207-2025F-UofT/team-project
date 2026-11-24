package view;

import interface_adapter.ViewWatchlist.ViewWatchlistController;
import interface_adapter.ViewWatchlist.WatchlistState;
import interface_adapter.ViewWatchlist.WatchlistViewModel;
import interface_adapter.ViewWatchlist.WatchlistViewModelListener;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URI;

/**
 * The watchlist page: shows every movie the user saved as a card (poster + title).
 * It listens to the WatchlistViewModel, so it redraws itself whenever the
 * View Watchlist presenter (or the Add to Watchlist presenter) updates the state.
 */
public class WatchlistPageView extends JPanel implements WatchlistViewModelListener {
    private static final int POSTER_WIDTH = 120;
    private static final int POSTER_HEIGHT = 180;
    private static final int COLUMNS = 4;

    private final WatchlistViewModel watchlistViewModel;
    private ViewWatchlistController viewWatchlistController;

    private final JLabel titleLabel = new JLabel("My Watchlist");
    private final JLabel messageLabel = new JLabel(" ");
    private final JPanel moviesPanel = new JPanel(new GridLayout(0, COLUMNS, 12, 12));

    public WatchlistPageView(WatchlistViewModel watchlistViewModel) {
        this.watchlistViewModel = watchlistViewModel;
        this.watchlistViewModel.addListener(this);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Top: page title + refresh button
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 22f));
        final JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(event -> loadWatchlist());

        final JPanel header = new JPanel(new BorderLayout());
        header.add(titleLabel, BorderLayout.WEST);
        header.add(refreshButton, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Middle: the grid of movie cards, scrollable
        final JPanel gridWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gridWrapper.add(moviesPanel);
        final JScrollPane scrollPane = new JScrollPane(gridWrapper);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom: empty-list / error message
        messageLabel.setForeground(Color.DARK_GRAY);
        add(messageLabel, BorderLayout.SOUTH);

        render(watchlistViewModel.getState());
    }

    public String getViewName() {
        return watchlistViewModel.getViewName();
    }

    public void setViewWatchlistController(ViewWatchlistController viewWatchlistController) {
        this.viewWatchlistController = viewWatchlistController;
    }

    /**
     * Asks the View Watchlist use case to (re)load the current user's watchlist.
     */
    public void loadWatchlist() {
        if (viewWatchlistController != null) {
            viewWatchlistController.execute(watchlistViewModel.getState().getUsername());
        }
    }

    @Override
    public void onWatchlistStateChanged(WatchlistState state) {
        // Presenters may run off the Swing thread, so always redraw on it.
        SwingUtilities.invokeLater(() -> render(state));
    }

    private void render(WatchlistState state) {
        final String username = state.getUsername();
        titleLabel.setText(username == null || username.isEmpty()
                ? "My Watchlist" : username + "'s Watchlist");

        moviesPanel.removeAll();
        for (WatchlistState.Item item : state.getItems()) {
            moviesPanel.add(createMovieCard(item));
        }

        if (!state.getErrorMessage().isEmpty()) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(state.getErrorMessage());
        }
        else if (state.getItems().isEmpty()) {
            messageLabel.setForeground(Color.DARK_GRAY);
            messageLabel.setText("Your watchlist is empty. Add a movie from its details page.");
        }
        else {
            messageLabel.setForeground(Color.DARK_GRAY);
            messageLabel.setText(state.getItems().size() + " movie(s) in your watchlist.");
        }

        moviesPanel.revalidate();
        moviesPanel.repaint();
    }

    private JPanel createMovieCard(WatchlistState.Item item) {
        final JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        final JLabel posterLabel = new JLabel("Loading...", SwingConstants.CENTER);
        posterLabel.setPreferredSize(new Dimension(POSTER_WIDTH, POSTER_HEIGHT));
        posterLabel.setOpaque(true);
        posterLabel.setBackground(new Color(230, 230, 230));
        loadPoster(item.getPosterUrl(), posterLabel);

        final JLabel nameLabel = new JLabel(
                "<html><div style='width:" + POSTER_WIDTH + "px;text-align:center'>"
                        + escapeHtml(item.getTitle()) + "</div></html>",
                SwingConstants.CENTER);

        card.add(posterLabel, BorderLayout.CENTER);
        card.add(nameLabel, BorderLayout.SOUTH);
        return card;
    }

    /**
     * Downloads the poster in the background so the page does not freeze.
     */
    private void loadPoster(String posterUrl, JLabel posterLabel) {
        if (posterUrl == null || posterUrl.isBlank()) {
            posterLabel.setText("No poster");
            return;
        }
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                final BufferedImage image = ImageIO.read(URI.create(posterUrl).toURL());
                if (image == null) {
                    return null;
                }
                return new ImageIcon(image.getScaledInstance(POSTER_WIDTH, POSTER_HEIGHT, Image.SCALE_SMOOTH));
            }

            @Override
            protected void done() {
                try {
                    final ImageIcon icon = get();
                    if (icon == null) {
                        posterLabel.setText("No poster");
                    }
                    else {
                        posterLabel.setText(null);
                        posterLabel.setIcon(icon);
                    }
                }
                catch (Exception ex) {
                    posterLabel.setText("No poster");
                }
            }
        }.execute();
    }

    private static String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
