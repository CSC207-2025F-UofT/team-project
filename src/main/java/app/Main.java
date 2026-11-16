package app;

import entity.Player;
import interface_adapter.game.GameController;
import interface_adapter.game.GamePresenter;
import interface_adapter.game.GameState;
import interface_adapter.game.GameViewModel;
import use_case.update_game.UpdateGameInteractor;
import view.GamePanel;
import view.GameWindow;
import view.KeyboardInput;

import javax.swing.SwingUtilities;

/** Builds every object, connects them, and starts the game. The only class that knows them all. */
public class Main {
    private static final int SCREEN_WIDTH = 960;
    private static final int SCREEN_HEIGHT = 640;
    private static final int PLAYER_SIZE = 48;
    private static final double PLAYER_SPEED = 4;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::start);
    }

    private static void start() {
        // Entities
        Player player = new Player((SCREEN_WIDTH - PLAYER_SIZE) / 2.0, (SCREEN_HEIGHT - PLAYER_SIZE) / 2.0,
                PLAYER_SIZE, PLAYER_SPEED);

        // Interface adapters + use case
        GameViewModel viewModel = new GameViewModel(
                new GameState((int) player.getX(), (int) player.getY(), player.getSize()));
        GamePresenter presenter = new GamePresenter(viewModel);
        UpdateGameInteractor updateGame = new UpdateGameInteractor(player, SCREEN_WIDTH, SCREEN_HEIGHT, presenter);
        GameController controller = new GameController(updateGame);

        // Frameworks: Swing window, input, loop
        KeyboardInput keyboard = new KeyboardInput();
        GamePanel panel = new GamePanel(viewModel, SCREEN_WIDTH, SCREEN_HEIGHT);
        panel.addKeyListener(keyboard);

        GameWindow window = new GameWindow(panel);
        window.setVisible(true);
        panel.requestFocusInWindow();

        new GameLoop(controller, keyboard, panel).start();
    }
}
