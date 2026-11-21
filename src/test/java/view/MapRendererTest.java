package view;

import interface_adapter.map.MapState;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Checks rendered pixels, cache invalidation and failures using images held in memory. */
class MapRendererTest {
    private final RecordingImageSource sprites = new RecordingImageSource();
    private final MapRenderer renderer = new MapRenderer(sprites);

    @Test
    void drawsRowsAndColumnsAtTheMapsOwnPixelDimensions() {
        int[][] ids = {{1, 0, 1}, {0, 1, 0}};

        BufferedImage image = renderer.render(new MapState(ids, 32, ""));

        assertEquals(96, image.getWidth());
        assertEquals(64, image.getHeight());
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color expected = Color.BLUE;
                if (ids[y / 32][x / 32] == 1) {
                    expected = Color.GREEN;
                }
                assertEquals(expected.getRGB(), image.getRGB(x, y));
            }
        }
        assertEquals("", renderer.getErrorMessage());
        assertEquals(6, sprites.lookups);
    }

    @Test
    void reusesTheImageForRepeatedStatesAndEquivalentSnapshots() {
        MapState state = new MapState(new int[][] {{1, 0}}, 32, "");
        BufferedImage first = renderer.render(state);

        assertSame(first, renderer.render(state));
        assertSame(first, renderer.render(new MapState(new int[][] {{1, 0}}, 32, "")));
        assertEquals(2, sprites.lookups);
    }

    @Test
    void changesErrorTextWithoutRedrawingUnchangedTerrain() {
        BufferedImage first = renderer.render(new MapState(new int[][] {{1, 0}}, 32, ""));

        assertSame(first, renderer.render(new MapState(new int[][] {{1, 0}}, 32, "Map unavailable.")));
        assertEquals("Map unavailable.", renderer.getErrorMessage());
        assertSame(first, renderer.render(new MapState(new int[][] {{1, 0}}, 32, "")));
        assertEquals("", renderer.getErrorMessage());
        assertEquals(2, sprites.lookups);
    }

    @Test
    void redrawsChangedIdsWithoutMutatingThePreviousImage() {
        BufferedImage first = renderer.render(new MapState(new int[][] {{1, 0}}, 32, ""));

        BufferedImage changed = renderer.render(new MapState(new int[][] {{0, 1}}, 32, ""));

        assertNotSame(first, changed);
        assertEquals(Color.GREEN.getRGB(), first.getRGB(0, 0));
        assertEquals(Color.BLUE.getRGB(), changed.getRGB(0, 0));
        assertEquals(Color.GREEN.getRGB(), changed.getRGB(32, 0));
        assertEquals(4, sprites.lookups);
    }

    @Test
    void redrawsWhenTheMapShapeChanges() {
        BufferedImage first = renderer.render(new MapState(new int[][] {{1, 0}}, 32, ""));

        BufferedImage changed = renderer.render(new MapState(new int[][] {{1}, {0}}, 32, ""));

        assertNotSame(first, changed);
        assertEquals(32, changed.getWidth());
        assertEquals(64, changed.getHeight());
        assertEquals(Color.BLUE.getRGB(), changed.getRGB(0, 32));
    }

    @Test
    void redrawsWhenTileSizeChangesWithoutFittingToAWindow() {
        BufferedImage first = renderer.render(new MapState(new int[][] {{1, 0}}, 32, ""));

        BufferedImage changed = renderer.render(new MapState(new int[][] {{1, 0}}, 16, ""));

        assertNotSame(first, changed);
        assertEquals(32, changed.getWidth());
        assertEquals(16, changed.getHeight());
        assertEquals(Color.GREEN.getRGB(), changed.getRGB(15, 15));
        assertEquals(Color.BLUE.getRGB(), changed.getRGB(16, 15));
    }

    @Test
    void handlesInitialStateAndLoadingFailureWithoutAnImage() {
        assertNull(renderer.render(new MapState()));
        assertEquals("", renderer.getErrorMessage());
        assertNull(renderer.render(new MapState(new int[0][0], 0, "Map unavailable.")));
        assertEquals("Map unavailable.", renderer.getErrorMessage());
        assertEquals(0, sprites.lookups);
    }

    @Test
    void canRenderAPreservedMapWhenAttachedAfterALoadingError() {
        BufferedImage image = renderer.render(new MapState(new int[][] {{1, 0}}, 32, "Map unavailable."));

        assertEquals(64, image.getWidth());
        assertEquals(Color.GREEN.getRGB(), image.getRGB(0, 0));
        assertEquals("Map unavailable.", renderer.getErrorMessage());
    }

    @Test
    void preservesTheLastCompleteImageOnRenderingFailureAndCanRecover() {
        BufferedImage first = renderer.render(new MapState(new int[][] {{1, 0}}, 32, ""));
        MapState invalid = new MapState(new int[][] {{0, 9}}, 32, "");

        assertSame(first, renderer.render(invalid));
        assertTrue(renderer.getErrorMessage().contains("Unable to render map"));
        assertTrue(renderer.getErrorMessage().contains("terrain ID: 9"));
        assertEquals(Color.GREEN.getRGB(), first.getRGB(0, 0));
        int lookupsAfterFailure = sprites.lookups;
        assertSame(first, renderer.render(invalid));
        assertEquals(lookupsAfterFailure, sprites.lookups);

        assertSame(first, renderer.render(new MapState(new int[][] {{1, 0}}, 32, "")));
        assertEquals("", renderer.getErrorMessage());
        assertEquals(lookupsAfterFailure, sprites.lookups);
    }

    @Test
    void doesNotExposeAPartialImageWhenTheFirstRenderFails() {
        assertNull(renderer.render(new MapState(new int[][] {{1, 9}}, 32, "")));
        assertTrue(renderer.getErrorMessage().contains("terrain ID: 9"));

        BufferedImage recovered = renderer.render(new MapState(new int[][] {{0}}, 32, ""));
        assertEquals(Color.BLUE.getRGB(), recovered.getRGB(0, 0));
        assertEquals("", renderer.getErrorMessage());
    }

    @Test
    void clearsTheCacheWhenTheMapIsRemoved() {
        MapState state = new MapState(new int[][] {{1}}, 32, "");
        BufferedImage first = renderer.render(state);

        assertNull(renderer.render(new MapState()));
        assertNotSame(first, renderer.render(state));
        assertEquals(2, sprites.lookups);
    }

    @Test
    void acceptsAdditionalTerrainIdsThroughTheImageSource() {
        BufferedImage image = renderer.render(new MapState(new int[][] {{42}}, 32, ""));

        assertEquals(Color.RED.getRGB(), image.getRGB(0, 0));
        assertEquals("", renderer.getErrorMessage());
    }

    @Test
    void rejectsAMissingImageSource() {
        assertThrows(IllegalArgumentException.class, () -> new MapRenderer(null));
    }

    /** Supplies fixed images and records lookups so cache reuse can be observed. */
    private static final class RecordingImageSource implements TileImageSource {
        private final Map<Integer, BufferedImage> images = Map.of(
                0, solidImage(Color.BLUE), 1, solidImage(Color.GREEN), 42, solidImage(Color.RED));
        private int lookups;

        @Override
        public BufferedImage getImage(int tileId) {
            lookups++;
            BufferedImage image = images.get(tileId);
            if (image == null) {
                throw new IllegalArgumentException("No tile image configured for terrain ID: " + tileId);
            }
            return image;
        }

        private static BufferedImage solidImage(Color color) {
            BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            try {
                graphics.setColor(color);
                graphics.fillRect(0, 0, 32, 32);
            } finally {
                graphics.dispose();
            }
            return image;
        }
    }
}
