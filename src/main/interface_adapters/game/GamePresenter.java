package main.interface_adapters.game;

import main.interface_adapters.ViewManagerModel;
import main.interface_adapters.settings.SettingsViewModel;
import main.view.GameView;

import java.util.Random;

public class GamePresenter {

    private final GameViewModel gameViewModel;
    private final SettingsViewModel settingsViewModel;
    private final ViewManagerModel viewManagerModel;

    public GamePresenter(GameViewModel gameViewModel, SettingsViewModel settingsViewModel,
                          ViewManagerModel viewManagerModel) {
        this.gameViewModel = gameViewModel;
        this.settingsViewModel = settingsViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    public void playerCheck() {
        gameViewModel.playerCheck();
    }

    public void gameStarted() {
        Random random = new Random();
        int num_players = gameViewModel.getPlayers();
        gameViewModel.setDealer(random.nextInt(num_players));
        gameViewModel.beginDealing();
    }
}
