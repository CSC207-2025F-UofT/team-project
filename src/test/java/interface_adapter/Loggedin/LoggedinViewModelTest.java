package interface_adapter.Loggedin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoggedinViewModelTest {

    private LoggedinViewModel viewModel;
    private int notifications;

    @BeforeEach
    void setUp() {
        viewModel = new LoggedinViewModel();
        notifications = 0;
    }

    @Test
    void viewNameIsLoggedIn() {
        assertEquals("logged in", LoggedinViewModel.VIEW_NAME);
    }

    @Test
    void startsWithDefaultState() {
        assertNotNull(viewModel.getState());
        assertFalse(viewModel.getState().isDataLoaded());
    }

    @Test
    void setStateReplacesStateWithoutNotifying() {
        viewModel.addListener(() -> notifications++);
        LoggedinState state = new LoggedinState();
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
