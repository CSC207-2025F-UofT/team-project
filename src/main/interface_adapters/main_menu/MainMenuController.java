package main.interface_adapters.main_menu;


public class MainMenuController {

    //private final MainMenuInputBoundary mainMenuUseCaseInteractor;
    private final MainMenuPresenter mainMenuPresenter; //temp

    public MainMenuController(MainMenuPresenter mainMenuPresenter) {
        //this.mainMenuUseCaseInteractor = mainMenuUseCaseInteractor
        this.mainMenuPresenter = mainMenuPresenter;
    }

    public void switchToLobbyView() {
        mainMenuPresenter.switchToLobbyView();
    }

    public void switchToSettingsView() {
        mainMenuPresenter.switchToSettingsView();
    }

    public void exitApplication() {
        mainMenuPresenter.exitApplication();
    }
}
