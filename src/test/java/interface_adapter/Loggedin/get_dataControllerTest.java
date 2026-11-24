package interface_adapter.Loggedin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.get_data.get_dataInputBoundary;
import use_case.get_data.get_dataInputData;

import static org.junit.jupiter.api.Assertions.*;

public class get_dataControllerTest {

    private static class SpyInteractor implements get_dataInputBoundary {
        get_dataInputData received = null;
        int executeCalls = 0;
        boolean switchedToGraph = false;

        @Override
        public void execute(get_dataInputData inputData) {
            received = inputData;
            executeCalls++;
        }

        @Override
        public void switchToUserViewGraphView() {
            switchedToGraph = true;
        }
    }

    private SpyInteractor interactor;
    private get_dataController controller;

    @BeforeEach
    void setUp() {
        interactor = new SpyInteractor();
        controller = new get_dataController(interactor);
    }

    @Test
    void executePassesUsernameToInteractor() {
        controller.execute("jonny");

        assertEquals(1, interactor.executeCalls);
        assertEquals("jonny", interactor.received.getUsername());
    }

    @Test
    void switchToUserViewGraphViewDelegatesToInteractor() {
        controller.switchToUserViewGraphView();

        assertTrue(interactor.switchedToGraph);
        assertEquals(0, interactor.executeCalls);
    }
}
