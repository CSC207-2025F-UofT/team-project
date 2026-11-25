package use_case.save;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SaveInteractorTest {

    // Fake storage: keeps username -> deepest floor in a HashMap instead of a file.
    private static class InMemorySaveDataAccess implements SaveDataAccessInterface {
        private final Map<String, Integer> floors = new HashMap<>();

        @Override
        public boolean hasSave(String username) {
            return floors.containsKey(username);
        }

        @Override
        public int getDeepestFloor(String username) {
            return floors.get(username);
        }

        @Override
        public void saveDeepestFloor(String username, int floor) {
            floors.put(username, floor);
        }
    }

    // Fake presenter: just remembers what the interactor told it.
    private static class RecordingPresenter implements SaveOutputBoundary {
        SaveOutputData successData;     // stays null unless success is called
        String errorMessage;            // stays null unless fail is called

        @Override
        public void prepareSuccessView(SaveOutputData outputData) {
            successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    @Test
    void firstSaveStoresFloor() {
        // Arrange
        InMemorySaveDataAccess dataAccess = new InMemorySaveDataAccess();
        RecordingPresenter presenter = new RecordingPresenter();
        SaveInteractor interactor = new SaveInteractor(dataAccess, presenter);

        // Act
        interactor.execute(new SaveInputData("darius", 3));

        // Assert
        assertNull(presenter.errorMessage);
        assertEquals(3, presenter.successData.getDeepestFloor());
        assertEquals(3, dataAccess.getDeepestFloor("darius"));
    }

    @Test
    void lowerFloorKeepsDeepest() {
        InMemorySaveDataAccess dataAccess = new InMemorySaveDataAccess();
        RecordingPresenter presenter = new RecordingPresenter();
        SaveInteractor interactor = new SaveInteractor(dataAccess, presenter);

        // Act: reach floor 5, then save again on floor 2
        interactor.execute(new SaveInputData("darius", 5));
        interactor.execute(new SaveInputData("darius", 2));

        // Assert: the best floor (5) is kept, not overwritten by 2
        assertNull(presenter.errorMessage);
        assertEquals(5, presenter.successData.getDeepestFloor());
        assertEquals(5, dataAccess.getDeepestFloor("darius"));
    }

    @Test
    void emptyUsernameFails() {
        // Arrange
        InMemorySaveDataAccess dataAccess = new InMemorySaveDataAccess();
        RecordingPresenter presenter = new RecordingPresenter();
        SaveInteractor interactor = new SaveInteractor(dataAccess, presenter);

        // Act
        interactor.execute(new SaveInputData("", 3));

        // Assert: it failed, never reported success, and stored nothing
        assertNotNull(presenter.errorMessage);
        assertNull(presenter.successData);
        assertFalse(dataAccess.hasSave(""));
    }
}
