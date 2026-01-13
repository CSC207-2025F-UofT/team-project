package use_case.openGame;
import entity.GameState;

public class OpenGameInteractor implements OpenGameInputBoundary {

    private final OpenGameOutputBoundary presenter;
    private final OpenGameDataAccessInterface dataAccess;
    private final ScreenSwitchBoundary screenSwitcher;

    public OpenGameInteractor(OpenGameOutputBoundary presenter,
                              OpenGameDataAccessInterface dataAccess,
                              ScreenSwitchBoundary screenSwitcher) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
        this.screenSwitcher = screenSwitcher;
    }

    @Override
    public void execute(OpenGameInputData inputData) {
        GameState state;

        if (inputData.isNewGame()) {
            // --- NEW GAME LOGIC ---
            state = new GameState(
                    inputData.getStartingLocation(),
                    inputData.getDestination()
            );

            // Save the newly created game
            dataAccess.saveGame(state);
            presenter.prepareSuccessView(
                    new OpenGameOutputData(
                            "New game started!",
                            state,
                            true
                    )
            );

//            // Prepare output
//            OpenGameOutputData output = new OpenGameOutputData(
//                    "New game started!",
//                    state,
//                    true
//            );

//            presenter.prepareSuccessView(output);

        } else {
            state = dataAccess.loadGame();
            // --- CONTINUE GAME LOGIC ---
            GameState saved = dataAccess.loadGame();

            if (state == null) {
                presenter.prepareFailView("No saved game found!");
                return;
            }
            presenter.prepareSuccessView(
                    new OpenGameOutputData(
                            "Game loaded!",
                            state,
                            false
                    )
            );

//            OpenGameOutputData output = new OpenGameOutputData(
//                    "Game loaded!",
//                    saved,
//                    false
//            );
//
//            presenter.prepareSuccessView(output);
        }
        if (state.isCompleted()) {
            screenSwitcher.switchToResultScreen();
        }
    }
}
