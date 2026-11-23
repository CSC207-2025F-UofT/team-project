package main.interface_adapters.lobby;

import com.sun.tools.javac.Main;
import main.interface_adapters.ViewManagerModel;
import main.interface_adapters.game.GameViewModel;
import main.interface_adapters.main_menu.MainMenuViewModel;
import main.interface_adapters.settings.SettingsViewModel;
import main.view.GameView;
import main.view.MainMenuView;

import java.util.List;

public class LobbyPresenter {

    private final ViewManagerModel viewManagerModel;
    private final LobbyViewModel lobbyViewModel;
    private final MainMenuViewModel mainMenuViewModel;
    private final GameViewModel gameViewModel;
    private final SettingsViewModel settingsViewModel;

    public LobbyPresenter(ViewManagerModel viewManagerModel, LobbyViewModel lobbyViewModel,
                          MainMenuViewModel mainMenuViewModel, GameViewModel gameViewModel,
                          SettingsViewModel settingsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.lobbyViewModel = lobbyViewModel;
        this.mainMenuViewModel = mainMenuViewModel;
        this.gameViewModel = gameViewModel;
        this.settingsViewModel = settingsViewModel;
    }

    public void switchToMainMenuView() {
        viewManagerModel.setActiveView(MainMenuViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    public void switchToGameView(int num_players) {
        viewManagerModel.setActiveView(GameViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
        List<String> names = List.of("Jim", "Janiel", "Janthony", "Jmarcus", "Jabriel", "Jomas");
        List<String> chips = List.of("1000", "1000", "1000", "1000", "1000", "1000");
        gameViewModel.saveOpponentData(names, chips);
        gameViewModel.setName(SettingsViewModel.USER_NAME);
        gameViewModel.setChipsHeld(SettingsViewModel.USER_MONEY);

        gameViewModel.setChipValues(lobbyViewModel.getMinBet());
        gameViewModel.chips.put(1, 10);
        gameViewModel.chips.put(5, 5);
        gameViewModel.chips.put(25, 5);
        gameViewModel.chips.put(50, 4);
        gameViewModel.chips.put(100, 2);
        gameViewModel.setPlayers(num_players);
        gameViewModel.startGame(); //MUST GO AT END
    }

    public void addPlayer() {
        if (lobbyViewModel.getPlayers() < 6) {
            lobbyViewModel.addPlayer();
        } else {
            ;
        }
    }

    public void removePlayer() {
        if (lobbyViewModel.getPlayers() > 2) {
            lobbyViewModel.removePlayer();
        } else {
            ;
        }
    }

    public void increaseMinBet() {
        if (lobbyViewModel.betAmounts.indexOf(lobbyViewModel.getMinBet()) < 6) {
            lobbyViewModel.increaseMinBet();
        } else {
            ;
        }
    }

    public void decreaseMinBet() {
        if (lobbyViewModel.betAmounts.indexOf(lobbyViewModel.getMinBet()) > 0) {
           lobbyViewModel.decreaseMinBet();
        } else {
            ;
        }
    }
}
