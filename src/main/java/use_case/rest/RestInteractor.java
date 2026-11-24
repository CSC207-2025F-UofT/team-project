package use_case.rest;

import entity.DailyWeather;
import entity.GameDay;
import entity.Player;

/**
 * UC4: the player rests, the game advances to the next day, energy is restored according to that day's weather,
 * and the completed progress is saved as a checkpoint.
 */
public class RestInteractor implements RestInputBoundary {
    static final String ACTIVITY_IN_PROGRESS_MESSAGE = "Finish or leave your challenge or exam before resting.";

    private final RestGameStateDataAccessInterface gameState;
    private final RestWeatherDataAccessInterface weatherSource;
    private final RestOutputBoundary presenter;

    public RestInteractor(RestGameStateDataAccessInterface gameState,
                          RestWeatherDataAccessInterface weatherSource,
                          RestOutputBoundary presenter) {
        this.gameState = gameState;
        this.weatherSource = weatherSource;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        if (gameState.getPlayer().hasActiveActivity()) {
            presenter.prepareFailView(ACTIVITY_IN_PROGRESS_MESSAGE);
            return;
        }
        GameDay newDay = gameState.getCurrentDay().next();
        gameState.setCurrentDay(newDay);

        DailyWeather weather = weatherSource.getWeather(newDay.getRealDate());
        gameState.setCurrentWeather(weather);
        Player player = gameState.getPlayer();
        player.restoreEnergyToPercent(weather.getEnergyRestorePercent());

        boolean saved = trySave();

        presenter.prepareSuccessView(new RestOutputData(
                newDay.getDayNumber(), newDay.getRealDate(), weather.getCondition(),
                weather.getHighTemperature(), weather.getLowTemperature(),
                player.getEnergy(), player.getMaxEnergy(), saved));
    }

    @Override
    public void retrySave() {
        presenter.prepareSaveRetryView(trySave());
    }

    private boolean trySave() {
        try {
            gameState.saveCheckpoint();
            return true;
        } catch (SaveFailedException e) {
            // UC4 extension 7a: the new day stays in the current session; the player is offered a retry.
            return false;
        }
    }
}
