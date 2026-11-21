package view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/** Loads and caches the images associated with explicitly configured terrain IDs. */
public final class TileSprites implements TileImageSource {
    private final Map<Integer, BufferedImage> images = new HashMap<>();

    /**
     * Loads each classpath image once, without retaining the supplied resource mapping.
     *
     * @param resourcePaths terrain IDs mapped to absolute classpath paths, such as /tiles/dirt.png
     * @param tileSize expected width and height of each square image in pixels
     * @throws IllegalArgumentException if configuration is null, contains invalid entries,
     *                                  or the expected size is not positive
     * @throws IOException if an image is missing, unreadable or has incorrect dimensions
     */
    public TileSprites(Map<Integer, String> resourcePaths, int tileSize) throws IOException {
        if (resourcePaths == null || tileSize <= 0) {
            throw new IllegalArgumentException("Tile resources must be supplied with a positive tile size.");
        }
        for (Map.Entry<Integer, String> entry : resourcePaths.entrySet()) {
            String path = entry.getValue();
            if (entry.getKey() == null || path == null || path.isBlank() || !path.startsWith("/")) {
                throw new IllegalArgumentException("Each tile ID needs an absolute classpath image path.");
            }
            images.put(entry.getKey(), loadImage(path, tileSize));
        }
    }

    /**
     * Returns the shared cached image for an ID. Callers must treat it as read-only.
     *
     * @throws IllegalArgumentException if no image was configured for this terrain ID
     */
    @Override
    public BufferedImage getImage(int tileId) {
        BufferedImage image = images.get(tileId);
        if (image == null) {
            throw new IllegalArgumentException("No tile image configured for terrain ID: " + tileId);
        }
        return image;
    }

    private BufferedImage loadImage(String path, int tileSize) throws IOException {
        try (InputStream stream = TileSprites.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IOException("Tile image resource not found: " + path);
            }
            BufferedImage image = ImageIO.read(stream);
            if (image == null) {
                throw new IOException("Resource is not a readable image: " + path);
            }
            if (image.getWidth() != tileSize || image.getHeight() != tileSize) {
                throw new IOException("Tile image " + path + " must be " + tileSize + " x " + tileSize
                        + " pixels, but is " + image.getWidth() + " x " + image.getHeight() + ".");
            }
            return image;
        } catch (IOException error) {
            throw new IOException("Could not load tile image '" + path + "': " + error.getMessage(), error);
        }
    }
}
