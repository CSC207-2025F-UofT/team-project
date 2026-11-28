package view;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

/** Saves reference photos and displays local or bundled listing photos. */
final class ListingPhoto {
    private static final Path DIRECTORY = Path.of(System.getProperty("user.home"), ".pawportal", "breed-photos");
    private static final String REFERENCE_LABEL = "TheCatAPI reference";
    private static final String DEMO_PREFIX = "classpath:/demo-cats/";

    private ListingPhoto() {
    }

    static Path saveReference(byte[] photo) throws IOException {
        Files.createDirectories(DIRECTORY);
        final Path path = DIRECTORY.resolve(UUID.randomUUID() + ".img");
        try {
            Files.write(path, photo);
        } catch (IOException exception) {
            discard(path);
            throw exception;
        }
        return path;
    }

    static void discard(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException exception) {
            // The listing was not changed. The unused file can be removed later.
        }
    }

    static boolean isReference(String path) {
        return !path.isBlank() && path.startsWith(DIRECTORY.toString() + java.io.File.separator);
    }

    static BufferedImage read(String path) throws IOException {
        if (path.startsWith(DEMO_PREFIX)) {
            try (InputStream resource = ListingPhoto.class.getResourceAsStream(path.substring("classpath:".length()))) {
                return resource == null ? null : ImageIO.read(resource);
            }
        }
        return ImageIO.read(Path.of(path).toFile());
    }

    static void show(JLabel label, String path, int width, int height) {
        final Object request = new Object();
        label.putClientProperty(ListingPhoto.class, request);
        label.setIcon(null);
        label.setText(path.isBlank() ? "No photo yet" : "Loading photo…");
        label.getAccessibleContext().setAccessibleDescription(null);
        if (path.isBlank()) {
            return;
        }
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws IOException {
                final BufferedImage image = read(path);
                if (image == null) {
                    return null;
                }
                final double scale = Math.min((double) width / image.getWidth(), (double) height / image.getHeight());
                final int fittedWidth = Math.max(1, (int) (image.getWidth() * scale));
                final int fittedHeight = Math.max(1, (int) (image.getHeight() * scale));
                return new ImageIcon(image.getScaledInstance(fittedWidth, fittedHeight, Image.SCALE_SMOOTH));
            }

            @Override
            protected void done() {
                if (label.getClientProperty(ListingPhoto.class) != request) {
                    return;
                }
                label.setText("Photo unavailable");
                try {
                    final ImageIcon icon = get();
                    if (icon != null) {
                        final String caption = path.startsWith(DEMO_PREFIX) ? "Demo photo"
                                : isReference(path) ? REFERENCE_LABEL : "";
                        label.setText(caption);
                        label.setIcon(icon);
                        label.getAccessibleContext().setAccessibleDescription(caption);
                    }
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException exception) {
                    // The saved path may refer to a photo that was moved or removed.
                }
            }
        }.execute();
    }
}
