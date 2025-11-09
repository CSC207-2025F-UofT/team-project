package interface_adapter.main_menu;

import use_case.switch_to_game.SwitchToGameViewInputBoundary;

/**
 * Controller for the Main Menu.
 */
public class MainMenuController {

    private final SwitchToGameViewInputBoundary switchToGameViewInteractor;

    public MainMenuController(SwitchToGameViewInputBoundary switchToGameViewInteractor) {
        this.switchToGameViewInteractor = switchToGameViewInteractor;
    }

    /**
     * Executes the switch to game view use case.
     */
    public void switchToGameView() {
        switchToGameViewInteractor.execute();
    }
}
