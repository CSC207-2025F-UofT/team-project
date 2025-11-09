package use_case.switch_to_game;

/**
 * The Switch to Game View Interactor.
 */
public class SwitchToGameViewInteractor implements SwitchToGameViewInputBoundary {
    private final SwitchToGameViewDataAccessInterface gameDataAccessObject;
    private final SwitchToGameViewOutputBoundary presenter;

    public SwitchToGameViewInteractor(SwitchToGameViewDataAccessInterface gameDataAccessInterface,
                                      SwitchToGameViewOutputBoundary switchToGameViewOutputBoundary) {
        this.gameDataAccessObject = gameDataAccessInterface;
        this.presenter = switchToGameViewOutputBoundary;
    }

    @Override
    public void execute() {
        // Simply switch to the game view
        presenter.switchToGameView();
    }
}
