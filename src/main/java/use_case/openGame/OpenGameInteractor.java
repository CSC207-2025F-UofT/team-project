//package use_case.openGame;
//import entity.GameState;
//
//public class OpenGameInteractor implements OpenGameInputBoundary {
//
//    private final OpenGameOutputBoundary presenter;
//    private final OpenGameDataAccessInterface dataAccess;
//    private final ScreenSwitchBoundary screenSwitcher;
//
//    public OpenGameInteractor(OpenGameOutputBoundary presenter,
//                              OpenGameDataAccessInterface dataAccess,
//                              ScreenSwitchBoundary screenSwitcher) {
//        this.presenter = presenter;
//        this.dataAccess = dataAccess;
//        this.screenSwitcher = screenSwitcher;
//    }
//
//    @Override
//    public void execute(OpenGameInputData inputData) {
//        GameState state;
//
//        if (inputData.isNewGame()) {
//            state = new GameState(
//                    inputData.getStartingLocation(),
//                    inputData.getDestination()
//            );
//
//            dataAccess.saveGame(state);
//
//            presenter.prepareSuccessView(
//                    new OpenGameOutputData(
//                            "New game started!",
//                            state,
//                            true
//                    )
//            );
//
//            // After preparing success view, switch to Move screen:
//            screenSwitcher.switchToMoveScreen();
//            return;
//        }
//
//        // CONTINUE GAME LOGIC
//        state = dataAccess.loadGame();
//
//        if (state == null) {
//            presenter.prepareFailView("No saved game found!");
//            return;
//        }
//        else {
//            // CONTINUE GAME LOGIC
//
//            if (!dataAccess.saveFileExists()) {
//                presenter.prepareFailView("No saved game to continue.");
//                return;
//            }
//
//            state = dataAccess.loadGame();
//
//            if (state == null) {
//                presenter.prepareFailView("Save file corrupted.");
//                return;
//            }
//
//            presenter.prepareSuccessView(
//                    new OpenGameOutputData(
//                            "Game loaded!",
//                            state,
//                            false
//                    )
//            );
//        }
//
//        presenter.prepareSuccessView(
//                new OpenGameOutputData(
//                        "Game loaded!",
//                        state,
//                        false
//                )
//        );
//
//        // Switch to Move screen
//        screenSwitcher.switchToMoveScreen();
//
//        // If game is already completed → go to results instead
//        if (state.isCompleted()) {
//            screenSwitcher.switchToResultScreen();
//        }
//    }
//    @Override
//    public void switchToMoveScreen() {
//        screenSwitcher.switchToMoveScreen();
//    }
//
//}


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

        // CASE 1: START NEW GAME
        if (inputData.isNewGame()) {

            if (dataAccess.saveFileExists()) {
                presenter.prepareFailView("A saved game already exists. Please click 'Continue Game'.");
                return;
            }

            GameState newState = new GameState(
                    inputData.getStartingLocation(),
                    inputData.getDestination()
            );

            dataAccess.saveGame(newState);

            presenter.prepareSuccessView(
                    new OpenGameOutputData(
                            "New game started!",
                            newState,
                            true
                    )
            );

            screenSwitcher.switchToMoveScreen();
            return;
        }

        // CASE 2: CONTINUE GAME
        if (!dataAccess.saveFileExists()) {
            presenter.prepareFailView("No saved game found. Please start a new game.");
            return;
        }

        GameState saved = dataAccess.loadGame();

        if (saved == null) {
            presenter.prepareFailView("Saved game is corrupted.");
            return;
        }

        presenter.prepareSuccessView(
                new OpenGameOutputData(
                        "Game loaded!",
                        saved,
                        false
                )
        );

        screenSwitcher.switchToMoveScreen();
    }

    @Override
    public void switchToMoveScreen() {
        screenSwitcher.switchToMoveScreen();
    }
}
