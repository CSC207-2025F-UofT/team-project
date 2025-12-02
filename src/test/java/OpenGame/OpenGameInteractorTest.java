package OpenGame;

import org.junit.jupiter.api.Test;
import use_case.openGame.*;

import static org.junit.jupiter.api.Assertions.*;

class OpenGameInteractorTest {

    // ---------- Fake Presenter ----------
    static class FakeOpenGamePresenter implements OpenGameOutputBoundary {

        String successMessage = null;
        String errorMessage = null;
        boolean switchedToMoveScreen = false;

        @Override
        public void prepareSuccessView(OpenGameOutputData outputData) {
            this.successMessage = outputData.getMessage();
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        @Override
        public void switchToMoveScreen() {
            this.switchedToMoveScreen = true;
        }
    }

    // ---------- Fake Data Access ----------
    static class FakeOpenGameDataAccess implements OpenGameDataAccessInterface {

        boolean saveExists;

        FakeOpenGameDataAccess(boolean saveExists) {
            this.saveExists = saveExists;
        }

        @Override
        public boolean saveFileExists() {
            return saveExists;
        }

        @Override
        public void deleteSaveFile() {
            // not needed for these tests
        }
    }

    // ---------- TESTS ----------

    @Test
    void startNewGame_whenNoSaveExists_successAndSwitchesView() {
        FakeOpenGamePresenter presenter = new FakeOpenGamePresenter();
        FakeOpenGameDataAccess dataAccess = new FakeOpenGameDataAccess(false);

        OpenGameInteractor interactor =
                new OpenGameInteractor(presenter, dataAccess);

        OpenGameInputData inputData = new OpenGameInputData(true);

        interactor.execute(inputData);

        assertEquals("New game started!", presenter.successMessage);
        assertNull(presenter.errorMessage);
        assertTrue(presenter.switchedToMoveScreen);
    }

    @Test
    void startNewGame_whenSaveExists_failsAndDoesNotSwitchView() {
        FakeOpenGamePresenter presenter = new FakeOpenGamePresenter();
        FakeOpenGameDataAccess dataAccess = new FakeOpenGameDataAccess(true);

        OpenGameInteractor interactor =
                new OpenGameInteractor(presenter, dataAccess);

        OpenGameInputData inputData = new OpenGameInputData(true);

        interactor.execute(inputData);

        assertEquals(
                "A saved game already exists. Please click 'Continue Game'.",
                presenter.errorMessage
        );
        assertNull(presenter.successMessage);
        assertFalse(presenter.switchedToMoveScreen);
    }

    @Test
    void continueGame_whenNoSaveExists_fails() {
        FakeOpenGamePresenter presenter = new FakeOpenGamePresenter();
        FakeOpenGameDataAccess dataAccess = new FakeOpenGameDataAccess(false);

        OpenGameInteractor interactor =
                new OpenGameInteractor(presenter, dataAccess);

        OpenGameInputData inputData = new OpenGameInputData(false);

        interactor.execute(inputData);

        assertEquals(
                "No saved game found. Please start a new game.",
                presenter.errorMessage
        );
        assertNull(presenter.successMessage);
        assertFalse(presenter.switchedToMoveScreen);
    }

    @Test
    void continueGame_whenSaveExists_successAndSwitchesView() {
        FakeOpenGamePresenter presenter = new FakeOpenGamePresenter();
        FakeOpenGameDataAccess dataAccess = new FakeOpenGameDataAccess(true);

        OpenGameInteractor interactor =
                new OpenGameInteractor(presenter, dataAccess);

        OpenGameInputData inputData = new OpenGameInputData(false);

        interactor.execute(inputData);

        assertEquals("Game loaded!", presenter.successMessage);
        assertNull(presenter.errorMessage);
        assertTrue(presenter.switchedToMoveScreen);
    }

    @Test
    void switchToMoveScreen_callsPresenter() {
        FakeOpenGamePresenter presenter = new FakeOpenGamePresenter();
        FakeOpenGameDataAccess dataAccess = new FakeOpenGameDataAccess(false);

        OpenGameInteractor interactor =
                new OpenGameInteractor(presenter, dataAccess);

        interactor.switchToMoveScreen();

        assertTrue(presenter.switchedToMoveScreen);
    }
}
