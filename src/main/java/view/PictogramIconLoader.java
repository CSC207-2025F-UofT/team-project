package view;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingWorker;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/** Loads pictogram images without blocking Swing's event dispatch thread. */
final class PictogramIconLoader {
    private static final int ICON_SIZE = 80;
    private static final int TIMEOUT_MILLIS = 3000;
    private final Map<String, ImageIcon> cache = new HashMap<>();

    /** Called on the event dispatch thread; labels remain usable without images. */
    void load(String location, JButton button) {
        ImageIcon cached = cache.get(location);
        if (cached != null) {
            button.setIcon(cached);
            return;
        }
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws IOException {
                URL url;
                if (location.startsWith("https://") || location.startsWith("http://")) {
                    url = new URL(location);
                } else {
                    String resource = location.startsWith("/") ? location : "/" + location;
                    url = PictogramIconLoader.class.getResource(resource);
                }
                if (url == null) {
                    return null;
                }
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(TIMEOUT_MILLIS);
                connection.setReadTimeout(TIMEOUT_MILLIS);
                try (InputStream stream = connection.getInputStream()) {
                    BufferedImage image = ImageIO.read(stream);
                    if (image == null) {
                        return null;
                    }
                    double scale = (double) ICON_SIZE / Math.max(image.getWidth(), image.getHeight());
                    int width = Math.max(1, (int) (image.getWidth() * scale));
                    int height = Math.max(1, (int) (image.getHeight() * scale));
                    return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
                }
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        cache.put(location, icon);
                        button.setIcon(icon);
                    }
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException exception) {
                    // Keep the text label as a usable fallback when an image fails.
                }
            }
        }.execute();
    }
}
