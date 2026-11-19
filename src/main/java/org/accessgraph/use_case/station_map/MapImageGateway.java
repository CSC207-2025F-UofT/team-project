package org.accessgraph.use_case.station_map;

import java.awt.image.BufferedImage;
import java.io.IOException;

/** Renders a map and its requested pins without depending on Swing components. */
@FunctionalInterface
public interface MapImageGateway {
    BufferedImage fetch(MapRequest request) throws IOException, InterruptedException;
}
