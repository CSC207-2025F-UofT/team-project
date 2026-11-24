package interface_adapter.Signup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SignupViewModelTest {

    private SignupViewModel viewModel;
    private int notifications;

    @BeforeEach
    void setUp() {
        viewModel = new SignupViewModel();
        notifications = 0;
    }

    @Test
    void viewNameIsSignUp() {
        assertEquals("sign up", SignupViewModel.VIEW_NAME);
    }

    @Test
    void startsWithDefaultState() {
        assertNotNull(viewModel.getState());
        assertEquals("", viewModel.getState().getUsername());
        assertFalse(viewModel.getState().isResearcher());
    }

    @Test
    void setStateReplacesStateWithoutNotifying() {
        viewModel.addListener(() -> notifications++);
        SignupState state = new SignupState();
        state.setUsername("jonny");

        viewModel.setState(state);

        assertSame(state, viewModel.getState());
        assertEquals(0, notifications);
    }

    @Test
    void notifyStateChangedCallsEveryListener() {
        int[] second = {0};
        viewModel.addListener(() -> notifications++);
        viewModel.addListener(() -> second[0]++);

        viewModel.notifyStateChanged();

        assertEquals(1, notifications);
        assertEquals(1, second[0]);
    }

    @Test
    void notifyWithNoListenersDoesNothing() {
        assertDoesNotThrow(() -> viewModel.notifyStateChanged());
    }
}
