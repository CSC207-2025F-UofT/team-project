package use_case.new_game;

import entity.GameState;
import org.junit.jupiter.api.Test;
import use_case.GameStateDataAccessInterface;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class NewGameInteractorTest {

    /** Fake DAO: records what the interactor saved. */
    private static class FakeGameStateDataAccess implements GameStateDataAccessInterface {
        private GameState saved;

        @Override
        public void saveGameState(GameState gameState) {
            saved = gameState;
        }

        @Override
        public GameState getGameState() {
            return saved;
        }
    }

    /** Fake presenter: records the output data it was given. */
    private static class FakePresenter implements NewGameOutputBoundary {
        private NewGameOutputData received;

        @Override
        public void prepareSuccessView(NewGameOutputData outputData) {
            received = outputData;
        }
    }

    @Test
    void executeSavesNewGameAndPresentsStartingStats() {
        FakeGameStateDataAccess dataAccess = new FakeGameStateDataAccess();
        FakePresenter presenter = new FakePresenter();
        NewGameInteractor interactor = new NewGameInteractor(dataAccess, presenter);

        interactor.execute(new NewGameInputData());

        GameState saved = dataAccess.getGameState();
        assertNotNull(saved);
        assertEquals(1, saved.getDay());
        assertEquals(GameState.START_LOCATION_ID, saved.getCurrentLocationId());

        NewGameOutputData output = presenter.received;
        assertNotNull(output);
        assertEquals(1, output.getDay());
        assertEquals(GameState.TOTAL_DAYS, output.getTotalDays());
        assertEquals(100, output.getEnergy());
        assertEquals(100, output.getSanity());
        assertEquals(50, output.getAcademicSurvival());
        assertEquals(50, output.getMoney());
    }

    @Test
    void executeReplacesAnExistingGame() {
        FakeGameStateDataAccess dataAccess = new FakeGameStateDataAccess();
        NewGameInteractor interactor = new NewGameInteractor(dataAccess, new FakePresenter());

        interactor.execute(new NewGameInputData());
        GameState first = dataAccess.getGameState();
        interactor.execute(new NewGameInputData());

        assertNotSame(first, dataAccess.getGameState());
    }
}
