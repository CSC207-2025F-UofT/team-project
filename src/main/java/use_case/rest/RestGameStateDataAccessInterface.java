package use_case.rest;

import entity.DailyWeather;
import entity.GameDay;
import entity.Player;

/**
 * Access to the current game state needed by the rest use case.
 */
public interface RestGameStateDataAccessInterface {
    GameDay getCurrentDay();

    void setCurrentDay(GameDay day);

    Player getPlayer();

    void setCurrentWeather(DailyWeather weather);

    /**
     * Saves the current game state as the checkpoint.
     * @throws SaveFailedException if the checkpoint could not be written
     */
    void saveCheckpoint() throws SaveFailedException;
}
