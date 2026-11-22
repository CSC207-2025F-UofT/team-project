package main.interface_adapters.lobby;

import com.sun.tools.javac.Main;
import main.interface_adapters.ViewManagerModel;
import main.interface_adapters.game.GameViewModel;
import main.interface_adapters.main_menu.MainMenuViewModel;
import main.view.MainMenuView;

public class LobbyPresenter {

    private final ViewManagerModel viewManagerModel;
    private final LobbyViewModel lobbyViewModel;
    private final MainMenuViewModel mainMenuViewModel;
    private final GameViewModel gameViewModel;

    public LobbyPresenter(ViewManagerModel viewManagerModel, LobbyViewModel lobbyViewModel,
                          MainMenuViewModel mainMenuViewModel, GameViewModel gameViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.lobbyViewModel = lobbyViewModel;
        this.mainMenuViewModel = mainMenuViewModel;
        this.gameViewModel = gameViewModel;
    }

    public void switchToMainMenuView() {
        viewManagerModel.setActiveView(MainMenuViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }
}
