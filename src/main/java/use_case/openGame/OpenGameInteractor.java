//package use_case.openGame;
//
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
//
//        // CASE 1: START NEW GAME
//        if (inputData.isNewGame()) {
//
//            if (dataAccess.saveFileExists()) {
//                presenter.prepareFailView("A saved game already exists. Please click 'Continue Game'.");
//                return;
//            }
//            //TODO: Can remove these?
//
//            GameState newState = new GameState(
//                    inputData.getStartingLocation(),
//                    inputData.getDestination()
//            );
//
//            // dataAccess.saveGame(newState);
//
//            presenter.prepareSuccessView(
//                    new OpenGameOutputData(
//                            "New game started!",
//                            newState,
//                            true
//                    )
//            );
//
//            screenSwitcher.switchToMoveScreen();
//            return;
//        }
//
//        // CASE 2: CONTINUE GAME
//        if (!dataAccess.saveFileExists()) {
//            presenter.prepareFailView("No saved game found. Please start a new game.");
//            return;
//        }
//
//        GameState saved = dataAccess.loadGame();
//
//        if (saved == null) {
//            presenter.prepareFailView("Saved game is corrupted.");
//            return;
//        }
//
//        presenter.prepareSuccessView(
//                new OpenGameOutputData(
//                        "Game loaded!",
//                        saved,
//                        false
//                )
//        );
//
//        screenSwitcher.switchToMoveScreen();
//    }
//
//    @Override
//    public void switchToMoveScreen() {
//        screenSwitcher.switchToMoveScreen();
//    }
//}

package use_case.openGame;

public class OpenGameInteractor implements OpenGameInputBoundary {

    private final OpenGameOutputBoundary presenter;
    private final OpenGameDataAccessInterface dataAccess;
    // private final ScreenSwitchBoundary screenSwitcher;

    //    public OpenGameInteractor(OpenGameOutputBoundary presenter,
//                              OpenGameDataAccessInterface dataAccess,
//                              ScreenSwitchBoundary screenSwitcher) {
//        this.presenter = presenter;
//        this.dataAccess = dataAccess;
//        this.screenSwitcher = screenSwitcher;
//    }
    public OpenGameInteractor(OpenGameOutputBoundary presenter,
                              OpenGameDataAccessInterface dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }


    @Override
    public void execute(OpenGameInputData inputData) {

        // CASE 1: START NEW GAME
        if (inputData.isNewGame()) {

            if (dataAccess.saveFileExists()) {
                presenter.prepareFailView("A saved game already exists. Please click 'Continue Game'.");
                return;
            }

            presenter.prepareSuccessView(
                    new OpenGameOutputData("New game started!" )
            );

//            screenSwitcher.switchToMoveScreen();
            presenter.switchToMoveScreen();
            return;
        }

        // CASE 2: CONTINUE GAME
        if (!dataAccess.saveFileExists()) {
            presenter.prepareFailView("No saved game found. Please start a new game.");
            return;
        }

        presenter.prepareSuccessView(
                new OpenGameOutputData("Game loaded!")
        );

        //screenSwitcher.switchToMoveScreen();
        presenter.switchToMoveScreen();
    }

    @Override
    public void switchToMoveScreen() {
//        screenSwitcher.switchToMoveScreen();
        presenter.switchToMoveScreen();
    }
}
