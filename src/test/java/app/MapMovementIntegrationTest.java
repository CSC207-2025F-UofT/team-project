package app;

import entity.Player;
import interface_adapter.game.GameController;
import interface_adapter.game.GamePresenter;
import interface_adapter.game.GameState;
import interface_adapter.game.GameViewModel;
import org.junit.jupiter.api.Test;
import use_case.update_game.UpdateGameInteractor;
import view.GamePanel;
import view.KeyboardInput;
import view.MapLayer;

import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/** Exercises the existing keyboard-to-player flow with the production map behind the player. */
class MapMovementIntegrationTest {
    @Test
    void supportsWasdAndArrowKeysWithTheMapVisible() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MapLayer layer = loadMap();
            int[][] cases = {
                    {KeyEvent.VK_W, 0, -4}, {KeyEvent.VK_UP, 0, -4},
                    {KeyEvent.VK_S, 0, 4}, {KeyEvent.VK_DOWN, 0, 4},
                    {KeyEvent.VK_A, -4, 0}, {KeyEvent.VK_LEFT, -4, 0},
                    {KeyEvent.VK_D, 4, 0}, {KeyEvent.VK_RIGHT, 4, 0}
            };
            for (int[] movement : cases) {
                Player player = new Player(456, 296, 48, 4);
                GameViewModel model = new GameViewModel(new GameState(456, 296, 48));
                GameController controller = new GameController(
                        new UpdateGameInteractor(player, 960, 640, new GamePresenter(model)));
                GamePanel panel = new GamePanel(model, 960, 640, layer);
                KeyboardInput keyboard = new KeyboardInput();

                keyboard.keyPressed(key(panel, KeyEvent.KEY_PRESSED, movement[0]));
                tick(controller, keyboard);
                keyboard.keyReleased(key(panel, KeyEvent.KEY_RELEASED, movement[0]));
                tick(controller, keyboard);

                int expectedX = 456 + movement[1];
                int expectedY = 296 + movement[2];
                assertEquals(expectedX, model.getState().getPlayerX());
                assertEquals(expectedY, model.getState().getPlayerY());
                BufferedImage image = paint(panel);
                assertEquals(Color.WHITE.getRGB(), image.getRGB(expectedX, expectedY));
                assertEquals(Color.BLUE.getRGB(), image.getRGB(24 * 32, 5 * 32));
                assertEquals(Color.GREEN.getRGB(), image.getRGB(0, 0));
            }
        });
    }

    @Test
    void doesNotAddPlayerWaterCollision() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MapLayer layer = loadMap();
            Player player = new Player(700, 128, 48, 4);
            GameViewModel model = new GameViewModel(new GameState(700, 128, 48));
            GameController controller = new GameController(
                    new UpdateGameInteractor(player, 960, 640, new GamePresenter(model)));
            GamePanel panel = new GamePanel(model, 960, 640, layer);
            KeyboardInput keyboard = new KeyboardInput();

            keyboard.keyPressed(key(panel, KeyEvent.KEY_PRESSED, KeyEvent.VK_D));
            tick(controller, keyboard);
            keyboard.keyReleased(key(panel, KeyEvent.KEY_RELEASED, KeyEvent.VK_D));

            assertEquals(704, model.getState().getPlayerX());
            BufferedImage image = paint(panel);
            assertEquals(Color.WHITE.getRGB(), image.getRGB(704, 128));
            assertEquals(Color.BLUE.getRGB(), image.getRGB(768, 128));
        });
    }

    private static MapLayer loadMap() {
        return new MapBuilder().build("level1.txt", 32,
                Map.of(0, "/tiles/water.png", 1, "/tiles/dirt.png"), message -> fail(message));
    }

    private static KeyEvent key(GamePanel panel, int eventId, int keyCode) {
        return new KeyEvent(panel, eventId, 0, 0, keyCode, KeyEvent.CHAR_UNDEFINED);
    }

    private static void tick(GameController controller, KeyboardInput keyboard) {
        controller.tick(keyboard.isUp(), keyboard.isDown(), keyboard.isLeft(), keyboard.isRight());
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
}
