package org.accessgraph.interface_adapter;

import org.accessgraph.use_case.station_map.MapRequest;

import java.awt.image.BufferedImage;
import java.io.IOException;

@FunctionalInterface
public interface MapImageGateway {
    BufferedImage fetch(MapRequest request) throws IOException, InterruptedException;
}
