package view;

import java.awt.image.BufferedImage;

/** Supplies already loaded tile images to view-layer rendering code. */
public interface TileImageSource {
    /**
     * Returns a non-null, already loaded image for a terrain ID without accessing files.
     * Callers must treat the returned image as read-only. Mappings stay fixed for this source.
     *
     * @param tileId the terrain ID supplied by the map display state
     * @return the image associated with this terrain ID
     * @throws IllegalArgumentException if no image is configured for this ID
     */
    BufferedImage getImage(int tileId);
}
