import entity.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.openGame.*;

import static org.junit.jupiter.api.Assertions.*;



public class OpenGameInteractorTest {
    @Test
    void testStartNewGameCreatesAndSavesState() {

        // --- Arrange (set up fake dependencies) ---

        FakeOutputBoundary fakePresenter = new FakeOutputBoundary();
        FakeDataAccess fakeDataAccess = new FakeDataAccess();
        FakeScreenSwitcher fakeSwitcher = new FakeScreenSwitcher();

        OpenGameInteractor interactor =
                new OpenGameInteractor(fakePresenter, fakeDataAccess, fakeSwitcher);

        // input: NEW GAME with starting + destination
        OpenGameInputData input =
                new OpenGameInputData(true, "StartVillage", "DragonCastle");

        // --- Act (run the use case) ---
        interactor.execute(input);

        // --- Assert (check what happened) ---

        // 1) Presenter should be called with success
        assertTrue(fakePresenter.successCalled, "Success view should be prepared");
        assertFalse(fakePresenter.failCalled, "Fail view should NOT be called");

        assertEquals("New game started!", fakePresenter.message);
        assertNotNull(fakePresenter.returnedState, "GameState should be passed to presenter");

        // 2) Data access should have saved the game
        assertNotNull(fakeDataAccess.savedState, "GameState should be saved in data access");
        assertEquals("StartVillage", fakeDataAccess.savedState.getCurrentLocation());
        assertEquals("DragonCastle", fakeDataAccess.savedState.getFinalDestination());

        // 3) Since a brand-new game is not completed yet,
        //    result screen should NOT be triggered
        assertFalse(fakeSwitcher.resultScreenCalled,
                "Result screen should NOT be switched to for a fresh new game");
    }

    // --------------------------------------------------------------------
    // Fake (stub) implementations used ONLY for this test
    // --------------------------------------------------------------------

    private static class FakeOutputBoundary implements OpenGameOutputBoundary {

        boolean successCalled = false;
        boolean failCalled = false;
        String message;
        GameState returnedState;

        @Override
        public void prepareSuccessView(OpenGameOutputData outputData) {
            successCalled = true;
            message = outputData.getMessage();
            returnedState = outputData.getGameState();
        }

        @Override
        public void prepareFailView(String errorMessage) {
            failCalled = true;
            message = errorMessage;
        }
    }

    private static class FakeDataAccess implements OpenGameDataAccessInterface {

        GameState savedState;

        @Override
        public GameState loadGame() {
            // not used in this test (only testing NEW game)
            return savedState;
        }

        @Override
        public void saveGame(GameState state) {
            this.savedState = state;
        }

        @Override
        public boolean saveFileExists() {
            // not relevant for this test
            return savedState != null;
        }

        @Override
        public void deleteSaveFile() {
            // not relevant for this test
            savedState = null;
        }
    }

    private static class FakeScreenSwitcher implements ScreenSwitchBoundary {

        boolean moveScreenCalled = false;
        boolean resultScreenCalled = false;

        @Override
        public void switchToMoveScreen() {
            moveScreenCalled = true;
        }

        @Override
        public void switchToResultScreen() {
            resultScreenCalled = true;
        }
    }
}
