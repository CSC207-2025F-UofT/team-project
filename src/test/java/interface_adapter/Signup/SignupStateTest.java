package interface_adapter.Signup;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SignupStateTest {

    @Test
    void defaults() {
        SignupState state = new SignupState();

        assertEquals("", state.getUsername());
        assertFalse(state.isResearcher());
        assertEquals("", state.getSignupError());
    }

    @Test
    void settersUpdateGetters() {
        SignupState state = new SignupState();
        state.setUsername("dr_smith");
        state.setResearcher(true);
        state.setSignupError("taken");

        assertEquals("dr_smith", state.getUsername());
        assertTrue(state.isResearcher());
        assertEquals("taken", state.getSignupError());
    }
}
