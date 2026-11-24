package interface_adapter.Signup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;

import static org.junit.jupiter.api.Assertions.*;

public class SignupControllerTest {

    private static class SpyInteractor implements SignupInputBoundary {
        SignupInputData received = null;
        int executeCalls = 0;
        boolean switchedToLogin = false;

        @Override
        public void execute(SignupInputData inputData) {
            received = inputData;
            executeCalls++;
        }

        @Override
        public void switchToLoginView() {
            switchedToLogin = true;
        }
    }

    private SpyInteractor interactor;
    private SignupController controller;

    @BeforeEach
    void setUp() {
        interactor = new SpyInteractor();
        controller = new SignupController(interactor);
    }

    @Test
    void executePassesRegularUserData() {
        controller.execute("jonny", false);

        assertEquals(1, interactor.executeCalls);
        assertEquals("jonny", interactor.received.getUsername());
        assertFalse(interactor.received.isResearcher());
    }

    @Test
    void executePassesResearcherData() {
        controller.execute("dr_smith", true);

        assertEquals("dr_smith", interactor.received.getUsername());
        assertTrue(interactor.received.isResearcher());
    }

    @Test
    void switchToLoginViewDelegatesToInteractor() {
        controller.switchToLoginView();

        assertTrue(interactor.switchedToLogin);
        assertEquals(0, interactor.executeCalls);
    }
}
