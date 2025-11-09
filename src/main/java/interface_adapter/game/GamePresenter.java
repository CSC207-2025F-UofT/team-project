package interface_adapter.game;

import use_case.click_button.ClickButtonOutputBoundary;
import use_case.click_button.ClickButtonOutputData;

/**
 * The Presenter for the Game View.
 */
public class GamePresenter implements ClickButtonOutputBoundary {

    private final GameViewModel gameViewModel;

    public GamePresenter(GameViewModel gameViewModel) {
        this.gameViewModel = gameViewModel;
    }

    @Override
    public void prepareView(ClickButtonOutputData outputData) {
        final GameState state = gameViewModel.getState();
        state.setMessage(outputData.getMessage());
        gameViewModel.firePropertyChange();
    }
}
