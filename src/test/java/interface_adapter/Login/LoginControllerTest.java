package interface_adapter.Login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;

import static org.junit.jupiter.api.Assertions.*;

public class LoginControllerTest {

    private static class SpyInteractor implements LoginInputBoundary {
        LoginInputData received = null;
        int executeCalls = 0;
        boolean switchedToSignup = false;

        @Override
        public void execute(LoginInputData inputData) {
            received = inputData;
            executeCalls++;
        }

        @Override
        public void switchToSignupView() {
            switchedToSignup = true;
        }
    }

    private SpyInteractor interactor;
    private LoginController controller;

    @BeforeEach
    void setUp() {
        interactor = new SpyInteractor();
        controller = new LoginController(interactor);
    }

    @Test
    void executePassesUsernameToInteractor() {
        controller.execute("jonny");

        assertEquals(1, interactor.executeCalls);
        assertEquals("jonny", interactor.received.getUsername());
    }

    @Test
    void executeDoesNotSetResearcherFlag() {
        controller.execute("jonny");

        assertFalse(interactor.received.isResearcher());
    }

    @Test
    void executePassesRawUsernameWithoutTrimming() {
        controller.execute("  jonny  ");

        assertEquals("  jonny  ", interactor.received.getUsername());
    }

    @Test
    void switchToSignupViewDelegatesToInteractor() {
        controller.switchToSignupView();

        assertTrue(interactor.switchedToSignup);
        assertEquals(0, interactor.executeCalls);
    }
}
