package org.accessgraph.use_case.station_map;

import java.awt.image.BufferedImage;
import java.util.Objects;

/** The rendered image and the viewport used to place its tiles and pins. */
public final class MapImage {
    private final BufferedImage image;
    private final MapViewport viewport;

    public MapImage(BufferedImage image, MapViewport viewport) {
        this.image = Objects.requireNonNull(image);
        this.viewport = Objects.requireNonNull(viewport);
    }

    public BufferedImage getImage() { return image; }
    public MapViewport getViewport() { return viewport; }
}
