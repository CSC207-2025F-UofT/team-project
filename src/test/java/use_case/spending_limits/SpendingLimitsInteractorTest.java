package use_case.spending_limits;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SpendingLimitsInteractorTest {

    private TestPresenter presenter;
    private TestDataAccess dataAccess;
    private SpendingLimitsInteractor interactor;

    @BeforeEach
    void setup() {
        presenter = new TestPresenter();
        dataAccess = new TestDataAccess();
        interactor = new SpendingLimitsInteractor(presenter, dataAccess);
    }


    @Test
    void testLoadLimitsSuccess() {
        dataAccess.mockLimits.put("SHOPPING", 200.0);

        interactor.loadLimits("user123");

        assertTrue(presenter.presentLimitsCalled);
        assertEquals(200.0, presenter.lastLimits.get("SHOPPING"));
    }

    @Test
    void testLoadLimitsEmpty() {

        interactor.loadLimits("user123");

        assertTrue(presenter.presentLimitsCalled);
        assertTrue(presenter.lastLimits.isEmpty());
    }

    @Test
    void testSaveLimitsSuccess() {
        Map<String, Double> limits = Map.of("DINING OUT", 120.0);

        interactor.saveLimits("user123", limits);

        assertTrue(dataAccess.saveCalled);
        assertTrue(presenter.saveSuccessCalled);
    }

    @Test
    void testSaveLimitsFailure() {
        dataAccess.throwErrorOnSave = true;

        interactor.saveLimits("user123", Map.of("SHOPPING", 150.0));

        assertTrue(presenter.saveFailureCalled);
        assertEquals("Save failed!", presenter.lastError);
    }


    static class TestPresenter implements SpendingLimitsOutputBoundary {
        boolean presentLimitsCalled = false;
        boolean saveSuccessCalled = false;
        boolean saveFailureCalled = false;

        Map<String, Double> lastLimits = new HashMap<>();
        String lastError = "";

        @Override
        public void presentLimits(Map<String, Double> limits) {
            presentLimitsCalled = true;
            lastLimits = limits;
        }

        @Override
        public void presentSaveSuccess() {
            saveSuccessCalled = true;
        }

        @Override
        public void presentSaveFailure(String error) {
            saveFailureCalled = true;
            lastError = error;
        }

        @Override
        public void prepareSuccessView() {}

        @Override
        public void prepareFailView(String error) {}
    }

    static class TestDataAccess implements SpendingLimitsDataAccessInterface {
        Map<String, Double> mockLimits = new HashMap<>();
        boolean saveCalled = false;
        boolean throwErrorOnSave = false;

        @Override
        public Map<String, Double> getSpendingLimitsByUsername(String username) {
            return mockLimits;
        }

        @Override
        public void saveUserSpendingLimits(String username, Map<String, Double> limits) {
            if (throwErrorOnSave) {
                throw new RuntimeException("Save failed!");
            }
            saveCalled = true;
        }
    }
}
