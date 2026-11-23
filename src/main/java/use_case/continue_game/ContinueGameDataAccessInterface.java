package use_case.continue_game;

import entity.DailyWeather;
import entity.GameDay;
import entity.Player;

/**
 * Access to the saved checkpoint needed by the continue-game use case.
 */
public interface ContinueGameDataAccessInterface {
    boolean hasCheckpoint();

    /**
     * Replaces the current game state with the saved checkpoint.
     * @throws InvalidCheckpointException if the checkpoint is damaged or unsupported; the file is left unchanged
     */
    void loadCheckpoint() throws InvalidCheckpointException;

    GameDay getCurrentDay();

    Player getPlayer();

    /** @return the current day's weather, or null if none has been retrieved yet */
    DailyWeather getCurrentWeather();
}
