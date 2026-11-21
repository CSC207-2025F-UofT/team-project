package view;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Checks the shipped placeholders and failures while loading tile image resources. */
class TileSpritesTest {
    @Test
    void loadsSolidOpaqueGreenDirtAndBlueWaterAtThirtyTwoPixels() throws IOException {
        TileSprites sprites = new TileSprites(Map.of(0, "/tiles/water.png", 1, "/tiles/dirt.png"), 32);

        assertSolidImage(sprites.getImage(0), Color.BLUE);
        assertSolidImage(sprites.getImage(1), Color.GREEN);
        assertSame(sprites.getImage(0), sprites.getImage(0));
        assertSame(sprites.getImage(1), sprites.getImage(1));
    }

    @Test
    void acceptsAdditionalIdsAndDoesNotRetainTheConfigurationMap() throws IOException {
        Map<Integer, String> paths = new HashMap<>();
        paths.put(42, "/tiles/dirt.png");
        TileImageSource sprites = new TileSprites(paths, 32);
        BufferedImage original = sprites.getImage(42);

        paths.put(42, "/tiles/water.png");
        paths.clear();

        assertSame(original, sprites.getImage(42));
        assertEquals(Color.GREEN.getRGB(), original.getRGB(0, 0));
    }

    @Test
    void reportsThePathOfAMissingImage() {
        IOException error = assertThrows(IOException.class,
                () -> new TileSprites(Map.of(0, "/tiles/missing.png"), 32));

        assertTrue(error.getMessage().contains("/tiles/missing.png"));
        assertTrue(error.getMessage().contains("not found"));
    }

    @Test
    void rejectsAResourceThatIsNotAnImage() {
        IOException error = assertThrows(IOException.class,
                () -> new TileSprites(Map.of(0, "/maps/level1.txt"), 32));

        assertTrue(error.getMessage().contains("/maps/level1.txt"));
        assertTrue(error.getMessage().contains("not a readable image"));
    }

    @Test
    void reportsThePathWhenDecodingACorruptPngFails() {
        IOException error = assertThrows(IOException.class,
                () -> new TileSprites(Map.of(0, "/tiles/corrupt.png"), 32));

        assertTrue(error.getMessage().contains("/tiles/corrupt.png"));
        assertTrue(error.getMessage().contains("Could not load tile image"));
    }

    @Test
    void rejectsImagesWithUnexpectedDimensions() {
        IOException error = assertThrows(IOException.class,
                () -> new TileSprites(Map.of(0, "/tiles/water.png"), 16));

        assertTrue(error.getMessage().contains("/tiles/water.png"));
        assertTrue(error.getMessage().contains("must be 16 x 16"));
        assertTrue(error.getMessage().contains("is 32 x 32"));
    }

    @Test
    void identifiesAnIdWithoutAnImageMapping() throws IOException {
        TileSprites sprites = new TileSprites(Map.of(1, "/tiles/dirt.png"), 32);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> sprites.getImage(9));

        assertTrue(error.getMessage().contains("terrain ID: 9"));
    }

    @Test
    void rejectsInvalidConfigurationBeforeLoadingImages() {
        assertThrows(IllegalArgumentException.class, () -> new TileSprites(null, 32));
        assertThrows(IllegalArgumentException.class, () -> new TileSprites(Map.of(), 0));
        assertThrows(IllegalArgumentException.class, () -> new TileSprites(Map.of(), -1));
        for (String path : new String[] {null, "", " \t", "tiles/dirt.png"}) {
            Map<Integer, String> paths = new HashMap<>();
            paths.put(1, path);
            assertThrows(IllegalArgumentException.class, () -> new TileSprites(paths, 32));
        }
        Map<Integer, String> paths = new HashMap<>();
        paths.put(null, "/tiles/dirt.png");
        assertThrows(IllegalArgumentException.class, () -> new TileSprites(paths, 32));
    }

    private void assertSolidImage(BufferedImage image, Color expected) {
        assertEquals(32, image.getWidth());
        assertEquals(32, image.getHeight());
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                assertEquals(expected.getRGB(), image.getRGB(x, y));
            }
        }
    }
}
