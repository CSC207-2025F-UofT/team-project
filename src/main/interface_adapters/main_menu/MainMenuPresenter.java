package main.interface_adapters.main_menu;

import main.interface_adapters.GameViewModel;
import main.interface_adapters.ViewManagerModel;

// implements MainMenuOutputBoundary
public class MainMenuPresenter {

    private final ViewManagerModel viewManagerModel;
    private final GameViewModel gameViewModel;

    public MainMenuPresenter(ViewManagerModel viewManagerModel, GameViewModel gameViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.gameViewModel = gameViewModel;
    }

    public void switchToGameView() {
        this.viewManagerModel.setActiveView(this.gameViewModel.VIEW_NAME);
        this.viewManagerModel.notifyViewChanged();
    }
}
