package use_case.CompleteGame;

import use_case.openGame.OpenGameDataAccessInterface;

public class CompleteGameInteractor implements CompleteGameInputBoundary {

    private final CompleteGameOutputBoundary presenter;
    private final OpenGameDataAccessInterface dataAccess;

    public CompleteGameInteractor(CompleteGameOutputBoundary presenter,
                                  OpenGameDataAccessInterface dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(CompleteGameInputData inputData) {

//        // If no save file exists → cannot delete → failure
//        if (!dataAccess.saveFileExists()) {
//            presenter.prepareFailView("No save file to delete.");
//            return;
//        }
//
//        // Delete file
//        dataAccess.deleteSaveFile();
//
//        // Output
//        CompleteGameOutputData output =
//                new CompleteGameOutputData(true);
//
//        presenter.prepareSuccessView(output);
//    }
        if (!inputData.isCompleted()) {
            presenter.prepareFailView("Game is not completed yet.");
            return;
        }

        if (!dataAccess.saveFileExists()) {
            presenter.prepareFailView("No save file found.");
            return;
        }

        dataAccess.deleteSaveFile();

        presenter.prepareSuccessView(
                new CompleteGameOutputData("Game Completed! Save file deleted.")
        );
    }
}
