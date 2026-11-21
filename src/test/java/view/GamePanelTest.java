package view;

import interface_adapter.game.GameState;
import interface_adapter.game.GameViewModel;
import interface_adapter.map.MapState;
import interface_adapter.map.MapViewModel;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Checks the actual panel's layer order, state updates and notification lifecycle on Swing's thread. */
class GamePanelTest {
    @Test
    void drawsALoadedMapBehindThePlayerWithoutStretchingIt() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MapViewModel map = loadedMap();
            GameViewModel game = new GameViewModel(new GameState(32, 0, 16));
            MapRenderer renderer = new MapRenderer(new RecordingImages());
            GamePanel panel = new GamePanel(game, 128, 96, new MapLayer(map, renderer));

            BufferedImage image = paint(panel);

            assertEquals(Color.GREEN.getRGB(), image.getRGB(0, 0));
            assertEquals(Color.WHITE.getRGB(), image.getRGB(32, 0));
            assertEquals(Color.BLUE.getRGB(), image.getRGB(48, 0));
            assertEquals(Color.BLUE.getRGB(), image.getRGB(0, 32));
            assertEquals(Color.BLACK.getRGB(), image.getRGB(100, 80));
        });
    }

    @Test
    void drawsAMapLoadedAfterThePanelWasConstructed() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MapViewModel map = new MapViewModel();
            GameViewModel game = new GameViewModel(new GameState(100, 80, 8));
            MapRenderer renderer = new MapRenderer(new RecordingImages());
            GamePanel panel = new GamePanel(game, 128, 96, new MapLayer(map, renderer));
            assertEquals(Color.BLACK.getRGB(), paint(panel).getRGB(0, 0));

            map.setState(new MapState(new int[][] {{0, 1}}, 32, ""));
            map.firePropertyChanged();

            assertEquals(Color.BLUE.getRGB(), paint(panel).getRGB(0, 0));
            assertEquals(Color.GREEN.getRGB(), paint(panel).getRGB(32, 0));
        });
    }

    @Test
    void movesThePlayerWithoutRedrawingTheMapTiles() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            RecordingImages sprites = new RecordingImages();
            GameViewModel game = new GameViewModel(new GameState(0, 0, 16));
            GamePanel panel = new GamePanel(game, 128, 96,
                    new MapLayer(loadedMap(), new MapRenderer(sprites)));
            paint(panel);

            game.setState(new GameState(32, 0, 16));
            BufferedImage image = paint(panel);
            paint(panel);

            assertEquals(Color.GREEN.getRGB(), image.getRGB(0, 0));
            assertEquals(Color.WHITE.getRGB(), image.getRGB(32, 0));
            assertEquals(4, sprites.lookups);
        });
    }

    @Test
    void displaysAnErrorAboveThePlayerAndKeepsThePreviousBackground() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MapViewModel map = loadedMap();
            MapRenderer renderer = new MapRenderer(new RecordingImages());
            GamePanel panel = new GamePanel(new GameViewModel(new GameState(0, 0, 16)), 960, 96,
                    new MapLayer(map, renderer));
            paint(panel);

            map.setState(new MapState(new int[][] {{1, 0}, {0, 1}}, 32, "Map unavailable."));
            BufferedImage failed = paint(panel);

            assertEquals("Map unavailable.", renderer.getErrorMessage());
            assertEquals(Color.BLACK.getRGB(), failed.getRGB(0, 0));
            assertEquals(Color.BLUE.getRGB(), failed.getRGB(0, 63));
            assertTrue(hasWhiteText(failed, 12, 12, 180, 32));

            map.setState(new MapState(new int[][] {{1, 0}, {0, 1}}, 32, ""));
            assertEquals(Color.WHITE.getRGB(), paint(panel).getRGB(0, 0));
        });
    }

    @Test
    void displaysRenderingErrorsWithoutDiscardingThePreviousMap() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MapViewModel map = loadedMap();
            MapRenderer renderer = new MapRenderer(new RecordingImages());
            GamePanel panel = new GamePanel(new GameViewModel(new GameState(100, 80, 8)), 960, 96,
                    new MapLayer(map, renderer));
            paint(panel);

            map.setState(new MapState(new int[][] {{9}}, 32, ""));
            BufferedImage failed = paint(panel);

            assertTrue(renderer.getErrorMessage().contains("terrain ID: 9"));
            assertEquals(Color.BLUE.getRGB(), failed.getRGB(0, 63));
            assertTrue(hasWhiteText(failed, 12, 12, 200, 32));
        });
    }

    @Test
    void wrapsLongErrorMessagesOntoAdditionalLines() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MapViewModel map = new MapViewModel();
            map.setState(new MapState(new int[0][0], 0,
                    "Unable to load map: unknown terrain at line 20, column 30.\nPlease check the map."));
            GamePanel panel = new GamePanel(new GameViewModel(new GameState(100, 100, 8)), 128, 128,
                    new MapLayer(map, new MapRenderer(new RecordingImages())));

            BufferedImage image = paint(panel);

            assertTrue(hasWhiteText(image, 12, 12, 116, 32));
            assertTrue(hasWhiteText(image, 12, 32, 116, 80));
        });
    }

    @Test
    void subscribesOnlyWhileAttachedAndAllowsBackgroundNotifications() throws Exception {
        MapViewModel map = loadedMap();
        CountingPanel[] panel = new CountingPanel[1];
        int[] before = new int[1];
        SwingUtilities.invokeAndWait(() -> {
            panel[0] = new CountingPanel(new GameViewModel(new GameState(0, 0, 16)),
                    new MapLayer(map, new MapRenderer(new RecordingImages())));
            panel[0].addNotify();
            before[0] = panel[0].repaints;
        });

        // This test thread is not the Swing event thread. repaint() remains safe to call here.
        map.firePropertyChanged();

        SwingUtilities.invokeAndWait(() -> {
            assertEquals(before[0] + 1, panel[0].repaints);
            panel[0].removeNotify();
            int detached = panel[0].repaints;
            map.firePropertyChanged();
            assertEquals(detached, panel[0].repaints);

            panel[0].addNotify();
            int reattached = panel[0].repaints;
            map.firePropertyChanged();
            assertEquals(reattached + 1, panel[0].repaints);
            panel[0].removeNotify();
        });
    }

    @Test
    void keepsTheOriginalCoreLoopConstructorWorking() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            GamePanel panel = new GamePanel(new GameViewModel(new GameState(0, 0, 16)), 128, 96);

            BufferedImage image = paint(panel);

            assertEquals(Color.WHITE.getRGB(), image.getRGB(0, 0));
            assertEquals(Color.BLACK.getRGB(), image.getRGB(32, 0));
        });
    }

    private static MapViewModel loadedMap() {
        MapViewModel model = new MapViewModel();
        model.setState(new MapState(new int[][] {{1, 0}, {0, 1}}, 32, ""));
        return model;
    }

    private static BufferedImage paint(GamePanel panel) {
        panel.setSize(panel.getPreferredSize());
        BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        try {
            panel.paint(graphics);
        } finally {
            graphics.dispose();
        }
        return image;
    }

    private static boolean hasWhiteText(BufferedImage image, int left, int top, int right, int bottom) {
        for (int y = top; y < bottom; y++) {
            for (int x = left; x < right; x++) {
                if (image.getRGB(x, y) == Color.WHITE.getRGB()) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Records repaint requests to check listener registration without opening a window. */
    private static final class CountingPanel extends GamePanel {
        private int repaints;

        private CountingPanel(GameViewModel game, MapLayer layer) {
            super(game, 128, 96, layer);
        }

        @Override
        public void repaint() {
            repaints++;
            super.repaint();
        }
    }

    /** Supplies fixed images and counts lookups to detect unnecessary map redraws. */
    private static final class RecordingImages implements TileImageSource {
        private final Map<Integer, BufferedImage> images = Map.of(
                0, solid(Color.BLUE), 1, solid(Color.GREEN));
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

        private static BufferedImage solid(Color color) {
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
