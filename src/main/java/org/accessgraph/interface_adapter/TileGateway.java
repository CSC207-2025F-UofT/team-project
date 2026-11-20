package org.accessgraph.interface_adapter;

import java.awt.image.BufferedImage;
import java.io.IOException;

/** Supplies one 256-pixel Web Mercator map tile. Implementations may block and are called off the UI thread. */
@FunctionalInterface
public interface TileGateway {
    BufferedImage tile(int zoom, int x, int y) throws IOException, InterruptedException;
}
