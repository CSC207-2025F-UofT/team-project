package interface_adapter.main_menu;

import interface_adapter.ViewManagerModel;
import interface_adapter.game.GameViewModel;
import use_case.switch_to_game.SwitchToGameViewOutputBoundary;

/**
 * The Presenter for the Main Menu.
 */
public class MainMenuPresenter implements SwitchToGameViewOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final GameViewModel gameViewModel;

    public MainMenuPresenter(ViewManagerModel viewManagerModel, GameViewModel gameViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.gameViewModel = gameViewModel;
    }

    @Override
    public void switchToGameView() {
        viewManagerModel.setState(gameViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
