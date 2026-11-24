package interface_adapter.Login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginViewModelTest {

    private LoginViewModel viewModel;
    private int notifications;

    @BeforeEach
    void setUp() {
        viewModel = new LoginViewModel();
        notifications = 0;
    }

    @Test
    void viewNameIsLogIn() {
        assertEquals("log in", LoginViewModel.VIEW_NAME);
    }

    @Test
    void startsWithDefaultState() {
        assertNotNull(viewModel.getState());
        assertEquals("", viewModel.getState().getUsername());
    }

    @Test
    void setStateReplacesStateWithoutNotifying() {
        viewModel.addListener(() -> notifications++);
        LoginState state = new LoginState();
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
