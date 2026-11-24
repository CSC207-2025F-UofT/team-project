package use_case.continue_game;

import entity.DailyWeather;
import entity.GameDay;
import entity.Player;

/**
 * UC5: restores the saved checkpoint so the player resumes at the residence.
 */
public class ContinueGameInteractor implements ContinueGameInputBoundary {
    static final String NO_CHECKPOINT_MESSAGE = "No saved game is available. Start a new game instead.";
    static final String INVALID_CHECKPOINT_MESSAGE = "The saved game cannot be loaded because it is damaged "
            + "or unsupported. The save file was left unchanged.";
    static final String ACTIVITY_IN_PROGRESS_MESSAGE = "Finish or leave your challenge or exam before "
            + "loading the saved game.";

    private final ContinueGameDataAccessInterface saveData;
    private final ContinueGameOutputBoundary presenter;

    public ContinueGameInteractor(ContinueGameDataAccessInterface saveData, ContinueGameOutputBoundary presenter) {
        this.saveData = saveData;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        if (saveData.getPlayer().hasActiveActivity()) {
            presenter.prepareFailView(ACTIVITY_IN_PROGRESS_MESSAGE);
            return;
        }
        if (!saveData.hasCheckpoint()) {
            presenter.prepareFailView(NO_CHECKPOINT_MESSAGE);
            return;
        }
        try {
            saveData.loadCheckpoint();
        } catch (InvalidCheckpointException e) {
            presenter.prepareFailView(INVALID_CHECKPOINT_MESSAGE);
            return;
        }

        GameDay day = saveData.getCurrentDay();
        Player player = saveData.getPlayer();
        DailyWeather weather = saveData.getCurrentWeather();
        presenter.prepareSuccessView(new ContinueGameOutputData(
                day.getDayNumber(), day.getRealDate(),
                weather == null ? null : weather.getCondition(),
                weather == null ? 0 : weather.getHighTemperature(),
                weather == null ? 0 : weather.getLowTemperature(),
                player.getEnergy(), player.getMaxEnergy()));
    }
}
