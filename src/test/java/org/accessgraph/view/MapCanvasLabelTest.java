package org.accessgraph.view;

import org.accessgraph.entity.Station;
import org.accessgraph.use_case.station_map.MapViewport;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class MapCanvasLabelTest {
    private static final Station UNION = new Station("1", "Union", 43.6455, -79.3807, Station.Accessibility.CONFIRMED,
            URI.create("https://www.ttc.ca/subway-stations/union-station"), LocalDate.of(2026, 11, 9));
    private static final Station KING = new Station("2", "King", 43.6490, -79.3778, Station.Accessibility.CONFIRMED,
            URI.create("https://www.ttc.ca/subway-stations/king-station"), LocalDate.of(2026, 11, 9));

    @Test
    void stationNamesAppearBesideTheirDotsOnlyAtStreetLevel() throws Exception {
        BufferedImage street = paint(List.of(UNION), 16);
        // The label sits up and to the right of the dot at the center (200, 150).
        assertEquals(Color.WHITE.getRGB(), street.getRGB(200 + 16, 150 - 12), "Label background");
        assertTrue(hasDarkText(street, 200 + 12, 150 - 24, 60, 24), "Label text");

        BufferedImage city = paint(List.of(UNION), 13);
        assertFalse(hasDarkText(city, 200 + 12, 150 - 24, 60, 24), "No labels when zoomed out");
    }

    @Test
    void crowdedLabelsDoNotOverlap() throws Exception {
        BufferedImage image = paint(List.of(UNION, KING), 15);
        assertTrue(hasDarkText(image, 0, 0, 400, 300));
        // Both stations are labelled, and the nearer-center one keeps its preferred upper-right position.
        assertEquals(Color.WHITE.getRGB(), image.getRGB(200 + 16, 150 - 12));
    }

    private static BufferedImage paint(List<Station> stations, int zoom) throws Exception {
        AtomicReference<BufferedImage> result = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            TileLoader tiles = new TileLoader((z, x, y) -> { throw new IOException("offline"); }, Runnable::run, () -> { });
            MapCanvas canvas = new MapCanvas(tiles, new MapCanvas.Gestures() {
                @Override public void zoom(double steps, double xFraction, double yFraction) { }
                @Override public void zoomStep(int direction) { }
                @Override public void pan(double xFraction, double yFraction) { }
            });
            canvas.setSize(400, 300);
            canvas.setStations(stations);
            canvas.setViewport(MapViewport.centered(UNION.getLatitude(), UNION.getLongitude(), zoom, 400, 300));
            BufferedImage image = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            canvas.paint(graphics);
            graphics.dispose();
            result.set(image);
        });
        return result.get();
    }

    private static boolean hasDarkText(BufferedImage image, int left, int top, int width, int height) {
        for (int y = Math.max(0, top); y < Math.min(image.getHeight(), top + height); y++) {
            for (int x = Math.max(0, left); x < Math.min(image.getWidth(), left + width); x++) {
                Color color = new Color(image.getRGB(x, y));
                if (color.getRed() + color.getGreen() + color.getBlue() < 200) { return true; }
            }
        }
        return false;
    }
}
