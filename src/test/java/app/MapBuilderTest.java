package app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import view.MapLayer;

import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Verifies startup preparation and error reporting without opening windows or dialogs. */
class MapBuilderTest {
    private static final Map<Integer, String> IMAGES = Map.of(0, "/tiles/water.png", 1, "/tiles/dirt.png");

    @Test
    void buildsTheProductionMapReadyToDraw() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            List<String> errors = new ArrayList<>();
            MapLayer layer = new MapBuilder().build("level1.txt", 32, IMAGES, message -> errors.add(message));

            assertNotNull(layer);
            assertTrue(errors.isEmpty());
            BufferedImage image = new BufferedImage(960, 640, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            try {
                layer.draw(graphics);
            } finally {
                graphics.dispose();
            }
            assertEquals(Color.GREEN.getRGB(), image.getRGB(480, 320));
            assertEquals(Color.BLUE.getRGB(), image.getRGB(24 * 32, 5 * 32));
            assertEquals(Color.GREEN.getRGB(), image.getRGB(959, 639));
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "missing-map.txt", "empty-map.txt", "invalid-map.txt", "uneven-map.txt"})
    void reportsAMapFailureOnceAndDoesNotBuildALayer(String name) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            List<String> errors = new ArrayList<>();
            MapLayer layer = new MapBuilder().build(name, 32, IMAGES, message -> errors.add(message));

            assertNull(layer);
            assertEquals(1, errors.size());
            assertTrue(errors.get(0).startsWith("Unable to load map:"));
        });
    }

    @Test
    void preservesTheLineAndColumnInAStartupMapError() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            List<String> errors = new ArrayList<>();
            new MapBuilder().build("invalid-map.txt", 32, IMAGES, message -> errors.add(message));

            assertEquals(1, errors.size());
            assertTrue(errors.get(0).contains("invalid-map.txt"));
            assertTrue(errors.get(0).contains("line 4, column 2"));
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"/tiles/missing.png", "/tiles/corrupt.png", "/maps/level1.txt"})
    void reportsAnImageFailureOnceAndDoesNotBuildALayer(String resource) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            List<String> errors = new ArrayList<>();
            MapLayer layer = new MapBuilder().build("level1.txt", 32,
                    Map.of(0, resource, 1, "/tiles/dirt.png"), message -> errors.add(message));

            assertNull(layer);
            assertEquals(1, errors.size());
            assertTrue(errors.get(0).contains(resource));
        });
    }

    @Test
    void reportsAnIncorrectImageSizeBeforeStartingTheGame() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            List<String> errors = new ArrayList<>();
            MapLayer layer = new MapBuilder().build("level1.txt", 16, IMAGES, message -> errors.add(message));

            assertNull(layer);
            assertEquals(1, errors.size());
            assertTrue(errors.get(0).contains("must be 16 x 16"));
        });
    }

    @Test
    void reportsAMissingTerrainImageMappingBeforeStartingTheGame() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            List<String> errors = new ArrayList<>();
            MapLayer layer = new MapBuilder().build("level1.txt", 32,
                    Map.of(1, "/tiles/dirt.png"), message -> errors.add(message));

            assertNull(layer);
            assertEquals(1, errors.size());
            assertTrue(errors.get(0).contains("Unable to render map:"));
            assertTrue(errors.get(0).contains("terrain ID: 0"));
        });
    }
}
