package interface_adapter.load;

import interface_adapter.ViewManagerModel;
import interface_adapter.game.GameState;
import interface_adapter.game.GameViewModel;
import use_case.load.LoadOutputData;
import use_case.load.LoadOutputBoundary;

public class LoadPresenter implements LoadOutputBoundary {
    GameViewModel gameViewModel;
    ViewManagerModel viewManagerModel;

    public LoadPresenter(ViewManagerModel viewManagerModel, GameViewModel gameViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.gameViewModel = gameViewModel;
    }

    @Override
    public void switchToLoadView(LoadOutputData loadOutputData) {
        // switch to view
        viewManagerModel.setState(gameViewModel.getViewName());
        viewManagerModel.firePropertyChange();

        final GameState state = gameViewModel.getState();
        state.setBackgroundImage(loadOutputData.getBackgroundImage());
        state.setClickableObjects(loadOutputData.getClickableObjects());
        gameViewModel.setState(state);
        gameViewModel.firePropertyChange();
    }
}
