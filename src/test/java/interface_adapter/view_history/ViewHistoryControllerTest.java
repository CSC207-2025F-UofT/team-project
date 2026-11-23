package interface_adapter.view_history;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewHistoryControllerTest {

    @Test
    void executeCallsTheInteractor() {
        int[] calls = {0};
        ViewHistoryController controller = new ViewHistoryController(() -> calls[0]++);

        controller.execute();

        assertEquals(1, calls[0]);
    }
}