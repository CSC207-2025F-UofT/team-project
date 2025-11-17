package interface_adapter.save;

import interface_adapter.ViewManagerModel;
import interface_adapter.game.GameState;
import interface_adapter.game.GameViewModel;
import use_case.save.SaveOutputBoundary;

/**
 * The Presenter for the Main Menu.
 */
public class SavePresenter implements SaveOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final GameViewModel gameViewModel;

    public SavePresenter(ViewManagerModel viewManagerModel, GameViewModel gameViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.gameViewModel = gameViewModel;
    }

    @Override
    public void switchToSaveView() {
        // TODO: make UI
    }
}
