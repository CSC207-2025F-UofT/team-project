package view;

import adapters.controller.MenuActions;
import adapters.viewmodel.GameViewModel;
import adapters.viewmodel.GameViewModel.CardType;
import adapters.viewmodel.GameViewModel.CardViewData;
import adapters.viewmodel.GameViewModel.TileViewData;
import adapters.viewmodel.LobbyViewModel;
import adapters.viewmodel.Screen;
import adapters.viewmodel.ViewState;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/** Opens the menus and a sample match while the controllers are being built. */
public final class GameViewDemo {
    private static final int SAMPLE_TURN_SECONDS = 120;

    private GameViewDemo() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameViewDemo::showDemo);
    }

    private static void showDemo() {
        JFrame frame = new JFrame("Spells to Save the World");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(createScreens(frame::dispose));
        frame.setSize(1120, 800);
        frame.setMinimumSize(new Dimension(860, 650));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static ViewManager createScreens(Runnable quit) {
        ViewState<Screen> screen = new ViewState<>(Screen.TITLE);
        ViewState<LobbyViewModel> lobby = new ViewState<>(LobbyViewModel.empty());
        ViewState<GameViewModel> gameState = new ViewState<>(sampleMatch());
        GameView game = new GameView();
        gameState.addPropertyChangeListener(game);
        game.render(gameState.getState());

        return new ViewManager(screen, lobby, game, new MenuActions() {
            private Timer opponentArrival;

            @Override
            public void showRules() {
                // TODO: Open the rules screen when one is available.
            }

            @Override
            public void quit() {
                quit.run();
            }

            @Override
            public void hostGame(String nickname) {
                simulateOpponent(Screen.HOST, nickname, "H7K2", true);
            }

            @Override
            public void joinGame(String nickname, String code) {
                simulateOpponent(Screen.JOIN, nickname, code, false);
            }

            @Override
            public void loadSave(String nickname) {
                // TODO: Open the load-save flow when it is available.
            }

            @Override
            public void leaveLobby() {
                stopOpponentArrival();
                lobby.setState(LobbyViewModel.empty());
            }

            @Override
            public void startGame() {
                screen.setState(Screen.GAME);
                startSampleMatch(game, gameState);
            }

            private void simulateOpponent(Screen source, String nickname, String code, boolean localHost) {
                stopOpponentArrival();
                lobby.setState(new LobbyViewModel(code, List.of(nickname), false));
                opponentArrival = new Timer(2500, event -> {
                    if (screen.getState() == source) {
                        List<String> players = localHost ? List.of(nickname, "Preview guest") : List.of("Preview host", nickname);
                        lobby.setState(new LobbyViewModel(code, players, localHost));
                        screen.setState(Screen.WAITING_TO_START);
                    }
                });
                opponentArrival.setRepeats(false);
                opponentArrival.start();
            }

            private void stopOpponentArrival() {
                if (opponentArrival != null) {
                    opponentArrival.stop();
                }
            }
        });
    }

    private static void startSampleMatch(GameView view, ViewState<GameViewModel> gameState) {
        //Test tile flashes and animation things
        ArrayList<Point> tilesToFlash = new ArrayList<Point>();
        tilesToFlash.add(new Point(3, 3));
        view.animateTrajectory(new Point(1, 1),  new Point(4, 4), 1000);
//        view.flashTiles(tilesToFlash, 3000);
        view.drawArrow(new Point(4, 1),  new Point(1, 4));
        view.setHighlightedTiles(tilesToFlash);

        long startedAt = System.nanoTime();
        new Timer(250, event -> {
            long elapsedSeconds = (System.nanoTime() - startedAt) / 1_000_000_000L;
            int remaining = SAMPLE_TURN_SECONDS - (int) elapsedSeconds;
            gameState.setState(gameState.getState().withRemainingTurnSeconds(remaining));
            if (remaining <= 0) {
                ((Timer) event.getSource()).stop();
            }
        }).start();
    }

    private static GameViewModel sampleMatch() {
        CardViewData kia = new CardViewData("Kia", CardType.CHARACTER, "Character", 200);
        CardViewData aki = new CardViewData("Aki", CardType.CHARACTER, "Character", 150);
        CardViewData magicMissiles = new CardViewData("Magic Missiles", CardType.SPELL, "Spell · 50 damage · Range 4");
        CardViewData fireball = new CardViewData("Fireball", CardType.SPELL, "Spell · 100 damage · Range 3");
        CardViewData mirror = new CardViewData("Mirror", CardType.SORCERY, "Sorcery · Reaction");

        List<TileViewData> board = new ArrayList<>(GameViewModel.emptyBoard());
        board.set(GameViewModel.tileIndex(1, 1), new TileViewData(kia, List.of(fireball), true));
        board.set(GameViewModel.tileIndex(3, 2), new TileViewData(aki, List.of(magicMissiles, fireball)));
        board.set(GameViewModel.tileIndex(4, 4), new TileViewData(mirror, List.of()));

        List<CardViewData> hand = List.of(
                new CardViewData("Cadence", CardType.CHARACTER, "Character", 250),
                fireball,
                magicMissiles,
                new CardViewData("Barrier", CardType.SPELL, "Spell"),
                new CardViewData("Cel", CardType.CHARACTER, "Character", 150));
        return new GameViewModel("Your turn", SAMPLE_TURN_SECONDS, board, hand, 31, 2, mirror);
    }
}
