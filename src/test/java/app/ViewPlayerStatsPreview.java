package app;

import entity.playerData;
import use_case.viewplayerstats.ViewPlayerStatsDataAccessInterface;
import use_case.viewplayerstats.ViewPlayerStatsSessionInterface;
import view.ViewPlayerStatsView;

import javax.swing.*;

/**
 * Runs the statistics screen with sample data for manual verification.
 */
public final class ViewPlayerStatsPreview {

    private ViewPlayerStatsPreview() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            playerData samplePlayer = createSamplePlayer();

            // Returns sample data when the requested player exists.
            ViewPlayerStatsDataAccessInterface dataAccess = playerId -> {
                if (samplePlayer.getId().equals(playerId)) {
                    return samplePlayer;
                }
                return null;
            };

            // Treats Eric as the current player for this preview.
            ViewPlayerStatsSessionInterface session =
                    () -> samplePlayer.getId();

            ViewPlayerStatsView view =
                    ViewPlayerStatsFactory.create(dataAccess, session);

            JFrame frame = new JFrame("Player Statistics - Preview");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setContentPane(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    /**
     * Creates sample statistics without saving an account
     * or playing games.
     */
    private static playerData createSamplePlayer() {
        playerData player = new playerData("Eric", "preview-only");
        player.setGamesPlayed(10);
        player.setGameWin(6);
        player.setUnoCallsMade(8);
        player.addAchievement("First Win");
        return player;
    }
}