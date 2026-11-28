package interface_adapter.logout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import use_case.logout.LogoutInputBoundary;

class LogoutControllerTest {

    @Test
    void passesTheRequestToTheInteractor() {
        final int[] calls = new int[1];
        final LogoutInputBoundary interactor = () -> calls[0]++;

        new LogoutController(interactor).execute();

        assertEquals(1, calls[0]);
    }
}
