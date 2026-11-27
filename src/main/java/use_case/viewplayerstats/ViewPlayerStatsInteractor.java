package use_case.viewplayerstats;

import entity.playerData;
import java.util.Objects;

/**
 * Retrieves the logged-in player's statistics
 * and delivers an output snapshot.
 */
public final class ViewPlayerStatsInteractor
        implements ViewPlayerStatsInputBoundary {

    private final ViewPlayerStatsDataAccessInterface dataAccess;
    private final ViewPlayerStatsSessionInterface session;
    private final ViewPlayerStatsOutputBoundary presenter;

    /**
     * Creates the use case with its required interfaces.
     *
     * @param dataAccess access to stored player data
     * @param session access to the current player's identity
     * @param presenter receives success or failure results
     */
    public ViewPlayerStatsInteractor(
            ViewPlayerStatsDataAccessInterface dataAccess,
            ViewPlayerStatsSessionInterface session,
            ViewPlayerStatsOutputBoundary presenter) {

        this.dataAccess = Objects.requireNonNull(dataAccess);
        this.session = Objects.requireNonNull(session);
        this.presenter = Objects.requireNonNull(presenter);
    }

    /**
     * Reads statistics without changing the player's data.
     *
     * @param inputData the request; identity comes from the session
     */
    @Override
    public void execute(ViewPlayerStatsInputData inputData) {
        String playerId = session.getCurrentPlayerId();

        if (playerId == null || playerId.isBlank()) {
            presenter.presentFailure(
                    "Please log in to view your statistics.");
            return;
        }

        playerData player;

        try {
            player = dataAccess.getPlayerData(playerId);
        } catch (IllegalStateException e) {
            presenter.presentFailure(
                    "Unable to load player statistics.");
            return;
        }

        if (player == null) {
            presenter.presentFailure(
                    "Player data was not found.");
            return;
        }

        int gamesPlayed = player.getGamesPlayed();

        // Avoid dividing by zero when no games have been played.
        float winRate = gamesPlayed == 0
                ? 0.0f
                : player.getWinRate();

        ViewPlayerStatsOutputData outputData =
                new ViewPlayerStatsOutputData(
                        playerId,
                        gamesPlayed,
                        player.getGameWin(),
                        winRate,
                        player.getUnoCallsMade(),
                        player.getAchievements());

        presenter.presentSuccess(outputData);
    }
}