package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;

/** A card listing the logged-in user's saved movies. */
public final class FavoritesPageView extends JPanel {
    public static final class MovieInfo {
        private final String title;
        private final String posterUrl;

        public MovieInfo(String title, String posterUrl) {
            this.title = title;
            this.posterUrl = posterUrl;
        }
    }

    private static final class Row {
        final int id;
        final String title;
        final BufferedImage poster;

        Row(int id, MovieInfo movie) {
            this.id = id;
            title = movie == null || movie.title == null || movie.title.isBlank()
                    ? "TMDB movie #" + id : movie.title;
            poster = movie == null ? null : loadPoster(movie.posterUrl);
        }
    }

    private static final Color PAPER = new Color(245, 247, 244);
    private static final Color INK = new Color(30, 54, 54);
    private static final Color LINE = new Color(208, 219, 215);
    private static final int GAP = 16;

    private static final class MovieCard extends JPanel {
        final JLabel poster;
        final JTextArea title;

        MovieCard(Row movie) {
            super(new BorderLayout(0, 8));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(LINE),
                    BorderFactory.createEmptyBorder(8, 8, 10, 8)));
            poster = new JLabel(movie.poster == null ? "No poster" : "", SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics graphics) {
                    super.paintComponent(graphics);
                    if (movie.poster != null) {
                        // Fill a portrait frame, trimming only the excess on the longer axis.
                        Graphics2D g = (Graphics2D) graphics.create();
                        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        double scale = Math.max((double) getWidth() / movie.poster.getWidth(),
                                (double) getHeight() / movie.poster.getHeight());
                        int w = (int) Math.ceil(movie.poster.getWidth() * scale);
                        int h = (int) Math.ceil(movie.poster.getHeight() * scale);
                        g.drawImage(movie.poster, (getWidth() - w) / 2, (getHeight() - h) / 2, w, h, null);
                        g.dispose();
                    }
                }
            };
            poster.setOpaque(true);
            poster.setBackground(new Color(230, 236, 232));
            poster.setForeground(INK);
            add(poster, BorderLayout.NORTH);
            title = new JTextArea(movie.title);
            title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
            title.setForeground(INK);
            title.setOpaque(false);
            title.setEditable(false);
            title.setFocusable(false);
            title.setLineWrap(true);
            title.setWrapStyleWord(true);
            add(title, BorderLayout.CENTER);
        }

        int measureHeight(int width) {
            Insets edges = getInsets();
            int inside = Math.max(1, width - edges.left - edges.right);
            int posterHeight = (inside * 3 + 1) / 2;
            if (poster.getPreferredSize().height != posterHeight) {
                poster.setPreferredSize(new Dimension(inside, posterHeight));
            }
            title.setSize(inside, Short.MAX_VALUE);
            return getPreferredSize().height;
        }
    }

    private final JPanel rows = new JPanel(null) {
        private int arrange(boolean apply) {
            int available = Math.max(1, getWidth());
            int columns = Math.max(1, (available + GAP) / (190 + GAP));
            int width = Math.min(232, (available - (columns - 1) * GAP) / columns);
            int top = 0;
            for (int start = 0; start < getComponentCount(); start += columns) {
                int end = Math.min(start + columns, getComponentCount());
                int height = 0;
                for (int i = start; i < end; i++) {
                    height = Math.max(height, ((MovieCard) getComponent(i)).measureHeight(width));
                }
                if (apply) {
                    for (int i = start; i < end; i++) {
                        getComponent(i).setBounds((i - start) * (width + GAP), top, width, height);
                    }
                }
                top += height + GAP;
            }
            return Math.max(0, top - GAP);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1, arrange(false));
        }

        @Override
        public void doLayout() {
            arrange(true);
        }
    };
    private final JLabel status = new JLabel();
    private final Function<String, List<Integer>> loadIds;
    private final IntFunction<MovieInfo> loadMovie;
    private final BiPredicate<String, Integer> removeMovie;
    private SwingWorker<Void, Row> worker;
    private Runnable goBack;

    public FavoritesPageView(Function<String, List<Integer>> loadIds, IntFunction<MovieInfo> loadMovie,
                             BiPredicate<String, Integer> removeMovie) {
        super(new BorderLayout(8, 8));
        this.loadIds = loadIds;
        this.loadMovie = loadMovie;
        this.removeMovie = removeMovie;
        setBackground(PAPER);
        setBorder(BorderFactory.createEmptyBorder(20, 22, 16, 22));
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, LINE),
                BorderFactory.createEmptyBorder(0, 0, 12, 0)));
        JLabel heading = new JLabel("My Favorites");
        heading.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        heading.setForeground(INK);
        JButton back = new JButton("Back");
        back.addActionListener(event -> {
            cancelLoad();
            goBack.run();
        });
        header.add(heading, BorderLayout.WEST);
        header.add(back, BorderLayout.EAST);
        rows.setBackground(PAPER);
        JScrollPane scroll = new JScrollPane(rows, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(PAPER);
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        rows.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                rows.revalidate();
            }
        });
        status.setForeground(INK);
        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
    }

    public void open(String username, Runnable goBack) {
        cancelLoad();
        this.goBack = goBack;
        rows.removeAll();
        rows.revalidate();
        rows.repaint();
        if (username == null || username.isBlank()) {
            status.setText("Please sign in to view your favorites.");
            return;
        }
        status.setText("Loading favorites...");
        worker = new SwingWorker<Void, Row>() {
            @Override
            protected Void doInBackground() {
                for (int id : loadIds.apply(username)) {
                    if (isCancelled()) {
                        break;
                    }
                    publish(new Row(id, loadMovie.apply(id)));
                }
                return null;
            }

            @Override
            protected void process(List<Row> batch) {
                if (worker == this && !isCancelled()) {
                    for (Row row : batch) {
                        addRow(username, row);
                    }
                    rows.revalidate();
                    rows.repaint();
                }
            }

            @Override
            protected void done() {
                if (worker != this || isCancelled()) {
                    return;
                }
                try {
                    get();
                    showCount();
                } catch (ExecutionException e) {
                    status.setText("Could not load favorites. Please try again.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        worker.execute();
    }

    private static BufferedImage loadPoster(String address) {
        if (address == null || address.isBlank() || address.endsWith("/w500")) {
            return null;
        }
        try {
            URLConnection connection = new URL(address).openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(5000);
            try (InputStream input = connection.getInputStream()) {
                return ImageIO.read(input);
            }
        } catch (IOException e) {
            return null;
        }
    }

    private void addRow(String username, Row movie) {
        MovieCard row = new MovieCard(movie);
        JButton remove = new JButton("Remove");
        remove.getAccessibleContext().setAccessibleName("Remove " + movie.title + " from favorites");
        remove.addActionListener(event -> {
            try {
                removeMovie.test(username, movie.id);
                rows.remove(row);
                rows.revalidate();
                rows.repaint();
                showCount();
            } catch (RuntimeException e) {
                status.setText("Could not remove this favorite. Please try again.");
            }
        });
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
        actions.setOpaque(false);
        actions.add(remove);
        row.add(actions, BorderLayout.SOUTH);
        rows.add(row);
    }

    private void showCount() {
        int count = rows.getComponentCount();
        status.setText(count == 0 ? "You have no favorites yet." : count + (count == 1 ? " movie" : " movies"));
    }

    private void cancelLoad() {
        if (worker != null) {
            worker.cancel(true);
            worker = null;
        }
    }
}
