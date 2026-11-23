package view;

import adapters.viewmodel.GameViewModel;
import adapters.viewmodel.GameViewModel.CardType;
import adapters.viewmodel.GameViewModel.CardViewData;
import adapters.viewmodel.GameViewModel.TileViewData;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** Opens a sample match while the gameplay controllers are being built. */
public final class GameViewDemo {
    private static final int SAMPLE_TURN_SECONDS = 120;

    private GameViewDemo() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameViewDemo::showMatch);
    }

    private static void showMatch() {
        GameView view = new GameView();
        GameViewModel sample = sampleMatch();
        view.render(sample);
        JFrame frame = new JFrame("Spells to Save the World");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(view);
        frame.setSize(1120, 800);
        frame.setMinimumSize(new Dimension(860, 650));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

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
            view.render(sample.withRemainingTurnSeconds(remaining));
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
