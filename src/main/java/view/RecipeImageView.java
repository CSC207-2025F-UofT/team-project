package view;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Displays a recipe image without blocking Swing's event dispatch thread.
 * Each instance belongs to one parent view and can be assigned a new URI.
 */
public final class RecipeImageView extends JPanel {
    private static final String PLACEHOLDER = "No image";
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(5))
            .readTimeout(Duration.ofSeconds(5))
            .callTimeout(Duration.ofSeconds(10))
            .build();
    private static final ExecutorService IMAGE_WORKERS = Executors.newFixedThreadPool(4, runnable -> {
        Thread thread = new Thread(runnable, "recipe-image-loader");
        thread.setDaemon(true);
        return thread;
    });

    private final JLabel imageLabel = new JLabel(PLACEHOLDER, SwingConstants.CENTER);
    private final int imageWidth;
    private final int imageHeight;
    private final ImageSource source;
    private SwingWorker<ImageIcon, Void> worker;
    private URI imageUri;
    private long requestNumber;

    /**
     * Creates an image view with a fixed display area.
     *
     * @param width the display area's width in pixels
     * @param height the display area's height in pixels
     * @throws IllegalArgumentException if width or height is not positive
     */
    public RecipeImageView(int width, int height) {
        this(width, height, RecipeImageView::readImage);
    }

    /**
     * Creates a view with an image source, allowing tests to avoid network access.
     *
     * @param width the display area's width in pixels
     * @param height the display area's height in pixels
     * @param source the image source
     * @throws IllegalArgumentException if width or height is not positive
     * @throws NullPointerException if source is null
     */
    RecipeImageView(int width, int height, ImageSource source) {
        super(new BorderLayout());
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Image dimensions must be positive");
        }
        this.imageWidth = width;
        this.imageHeight = height;
        this.source = Objects.requireNonNull(source);
        setPreferredSize(new Dimension(width, height));
        add(imageLabel, BorderLayout.CENTER);
    }

    /**
     * Displays an image asynchronously or a placeholder if it is unavailable.
     * Call this method on Swing's event dispatch thread. A newer URI always
     * takes precedence over an earlier request that is still loading.
     *
     * @param uri an HTTP, HTTPS, file, or JAR URI; null displays the placeholder
     */
    public void setImage(URI uri) {
        imageUri = uri;
        cancelCurrentRequest();
        imageLabel.setIcon(null);
        imageLabel.setText(PLACEHOLDER);
        if (!isSupported(uri)) {
            return;
        }

        long currentRequest = requestNumber;
        worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws IOException {
                BufferedImage image = source.read(uri);
                return image == null ? null : new ImageIcon(scaleToFit(image));
            }

            @Override
            protected void done() {
                if (currentRequest != requestNumber || isCancelled()) {
                    return;
                }
                worker = null;
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        imageLabel.setText("");
                        imageLabel.setIcon(icon);
                    }
                }
                catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                }
                catch (ExecutionException | CancellationException exception) {
                    /*
                     * Retain the placeholder when the image cannot be loaded.
                     */
                }
            }
        };
        IMAGE_WORKERS.execute(worker);
    }

    /**
     * Restarts a cancelled load if the component is added back to a container.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        if (worker == null && imageLabel.getIcon() == null && isSupported(imageUri)) {
            setImage(imageUri);
        }
    }

    /**
     * Cancels an outstanding load when the component leaves its container.
     */
    @Override
    public void removeNotify() {
        cancelCurrentRequest();
        super.removeNotify();
    }

    private void cancelCurrentRequest() {
        requestNumber++;
        if (worker != null) {
            worker.cancel(true);
            worker = null;
        }
    }

    private BufferedImage scaleToFit(BufferedImage image) {
        double scale = Math.min((double) imageWidth / image.getWidth(),
                (double) imageHeight / image.getHeight());
        int width = Math.max(1, (int) Math.round(image.getWidth() * scale));
        int height = Math.max(1, (int) Math.round(image.getHeight() * scale));
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = scaled.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.drawImage(image, 0, 0, width, height, null);
        }
        finally {
            graphics.dispose();
        }
        return scaled;
    }

    private static boolean isSupported(URI uri) {
        return uri != null && ("http".equalsIgnoreCase(uri.getScheme())
                || "https".equalsIgnoreCase(uri.getScheme()) || "file".equalsIgnoreCase(uri.getScheme())
                || "jar".equalsIgnoreCase(uri.getScheme()));
    }

    private static BufferedImage readImage(URI uri) throws IOException {
        if ("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme())) {
            Request request = new Request.Builder().url(uri.toString()).get().build();
            try (Response response = CLIENT.newCall(request).execute()) {
                ResponseBody body = response.body();
                if (!response.isSuccessful() || body == null) {
                    throw new IOException("Unable to load the recipe image");
                }
                return ImageIO.read(body.byteStream());
            }
        }

        URLConnection connection = uri.toURL().openConnection();
        connection.setUseCaches(false);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        try (InputStream stream = connection.getInputStream()) {
            return ImageIO.read(stream);
        }
    }

    /**
     * Reads an image from a URI.
     */
    @FunctionalInterface
    interface ImageSource {
        /**
         * Reads an image without changing Swing components.
         *
         * @param uri the image location
         * @return the decoded image, or null if the format is unsupported
         * @throws IOException if the image cannot be read
         */
        BufferedImage read(URI uri) throws IOException;
    }
}
