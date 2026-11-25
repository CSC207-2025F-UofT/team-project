package usecase.startgame;

import entity.Game;
import entity.Player;

import java.util.ArrayList;

public class StartGameInteractor implements StartGameInputBoundary {
    private static final int INITIAL_HAND_SIZE = 5;

    private final StartGameOutputBoundary presenter;

    public StartGameInteractor(StartGameOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void startGame(String player1Name, String player2Name) {
        String n1 = player1Name == null ? "" : player1Name.trim();
        String n2 = player2Name == null ? "" : player2Name.trim();

        if (n1.isEmpty() || n2.isEmpty()) {
            presenter.presentStartFailure("Both player names are required.");
            return;
        }
        if (n1.equals(n2)) {
            presenter.presentStartFailure("Player names must be different.");
            return;
        }

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(n1));
        players.add(new Player(n2));

        Game game = new Game(players);
        game.dealInitialHands(INITIAL_HAND_SIZE);

        presenter.presentGameStarted(game);
    }
}
