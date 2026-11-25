package use_case.load;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LoadInteractorTest {

    private static class InMemoryLoadDataAccess implements LoadDataAccessInterface {
        private final Map<String, Integer> floors = new HashMap<>();

        void put(String username, int floor) {   // helper for setting up tests
            floors.put(username, floor);
        }

        @Override
        public boolean hasSave(String username) {
            return floors.containsKey(username);
        }

        @Override
        public int getDeepestFloor(String username) {
            return floors.get(username);
        }
    }

    private static class RecordingPresenter implements LoadOutputBoundary {
        LoadOutputData successData;
        String errorMessage;

        @Override
        public void prepareSuccessView(LoadOutputData outputData) {
            successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    @Test
    void loadsExistingSave() {
        InMemoryLoadDataAccess dataAccess = new InMemoryLoadDataAccess();
        dataAccess.put("darius", 5);
        RecordingPresenter presenter = new RecordingPresenter();
        LoadInteractor interactor = new LoadInteractor(dataAccess, presenter);

        interactor.execute(new LoadInputData("darius"));

        assertNull(presenter.errorMessage);
        assertEquals("darius", presenter.successData.getUsername());
        assertEquals(5, presenter.successData.getFloor());
    }

    @Test
    void noSaveFails() {
        InMemoryLoadDataAccess dataAccess = new InMemoryLoadDataAccess();
        RecordingPresenter presenter = new RecordingPresenter();
        LoadInteractor interactor = new LoadInteractor(dataAccess, presenter);

        interactor.execute(new LoadInputData("nobody"));

        assertNotNull(presenter.errorMessage);
        assertNull(presenter.successData);
    }

    @Test
    void emptyUsernameFails() {
        InMemoryLoadDataAccess dataAccess = new InMemoryLoadDataAccess();
        RecordingPresenter presenter = new RecordingPresenter();
        LoadInteractor interactor = new LoadInteractor(dataAccess, presenter);

        interactor.execute(new LoadInputData(""));

        assertNotNull(presenter.errorMessage);
        assertNull(presenter.successData);
    }
}