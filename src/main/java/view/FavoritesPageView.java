package view;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.IntFunction;

/** A reusable card showing the current user's saved movies. */
public final class FavoritesPageView extends JPanel {
    private final DefaultListModel<String> movies = new DefaultListModel<>();
    private final JLabel status = new JLabel();
    private final Function<String, List<Integer>> loadIds;
    private final IntFunction<String> titleForId;
    private SwingWorker<Void, String> worker;
    private Runnable goBack;

    public FavoritesPageView(Function<String, List<Integer>> loadIds, IntFunction<String> titleForId) {
        super(new BorderLayout(8, 8));
        this.loadIds = loadIds;
        this.titleForId = titleForId;
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JButton back = new JButton("Back");
        back.addActionListener(event -> {
            cancelLoad();
            goBack.run();
        });
        header.add(back);
        header.add(new JLabel("My Favorites"));
        add(header, BorderLayout.NORTH);
        add(new JScrollPane(new JList<>(movies)), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
    }

    /** Refresh on every visit, including after adding a movie or changing users. */
    public void open(String username, Runnable goBack) {
        cancelLoad();
        this.goBack = goBack;
        movies.clear();
        if (username == null || username.isBlank()) {
            status.setText("Please sign in to view your favorites.");
            return;
        }

        status.setText("Loading favorites...");
        worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() {
                for (int id : loadIds.apply(username)) {
                    if (isCancelled()) {
                        break;
                    }
                    String title = titleForId.apply(id);
                    publish(title == null || title.isBlank() ? "TMDB movie #" + id : title);
                }
                return null;
            }

            @Override
            protected void process(List<String> titles) {
                if (worker == this && !isCancelled()) {
                    titles.forEach(movies::addElement);
                }
            }

            @Override
            protected void done() {
                if (worker != this || isCancelled()) {
                    return;
                }
                try {
                    get();
                    status.setText(movies.isEmpty() ? "You have no favorites yet." : "");
                } catch (ExecutionException e) {
                    status.setText("Could not load favorites. Please try again.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        worker.execute();
    }

    private void cancelLoad() {
        if (worker != null) {
            worker.cancel(true);
            worker = null;
        }
    }
}
