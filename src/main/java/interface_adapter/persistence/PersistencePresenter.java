package interface_adapter.persistence;

import interface_adapter.ViewManagerModel;
import interface_adapter.hud.HudState;
import interface_adapter.hud.HudViewModel;
import use_case.ManageSavesInteractor;
import use_case.SavedGameSummary;
import use_case.load_game.LoadGameOutputBoundary;
import use_case.load_game.LoadGameOutputData;
import use_case.save_game.SaveGameOutputBoundary;

import javax.swing.SwingUtilities;
import java.util.List;

public final class PersistencePresenter implements SaveGameOutputBoundary, LoadGameOutputBoundary,
        ManageSavesInteractor.OutputBoundary {
    private final PersistenceViewModel feedback;
    private final HudViewModel hud;
    private final ViewManagerModel views;
    private final String gameViewName;

    public PersistencePresenter(PersistenceViewModel feedback, HudViewModel hud,
                                ViewManagerModel views, String gameViewName) {
        this.feedback = feedback;
        this.hud = hud;
        this.views = views;
        this.gameViewName = gameViewName;
    }

    @Override
    public void prepareSaveSuccessView() {
        showMessage("Game saved.");
    }

    @Override
    public void prepareNameView() {
        onEdt(() -> {
            updateMessage("");
            feedback.firePropertyChanged(PersistenceViewModel.REQUEST_NAME);
        });
    }

    @Override
    public void prepareSavedGamesView(List<SavedGameSummary> saves, String message) {
        onEdt(() -> {
            feedback.setSaves(saves);
            updateMessage(message);
            feedback.firePropertyChanged(PersistenceViewModel.SAVES_UPDATED);
        });
    }

    @Override
    public void prepareLoadSuccessView(LoadGameOutputData output) {
        onEdt(() -> {
            HudState state = new HudState();
            state.setDay(output.day());
            state.setTotalDays(output.totalDays());
            state.setEnergy(output.energy());
            state.setSanity(output.sanity());
            state.setAcademicSurvival(output.academicSurvival());
            state.setMoney(output.money());
            hud.setState(state);
            hud.firePropertyChanged();
            views.setState(gameViewName);
            views.firePropertyChanged();
            updateMessage("Game loaded.");
            feedback.firePropertyChanged(PersistenceViewModel.GAME_LOADED);
        });
    }

    @Override
    public void prepareFailView(String message) {
        showMessage(message);
    }

    private void showMessage(String message) {
        onEdt(() -> updateMessage(message));
    }

    private void updateMessage(String message) {
        feedback.setState(new PersistenceViewModel.State(false, message));
        feedback.firePropertyChanged();
    }

    private void onEdt(Runnable update) {
        if (SwingUtilities.isEventDispatchThread()) {
            update.run();
        } else {
            SwingUtilities.invokeLater(update);
        }
    }
}
