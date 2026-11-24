package interface_adapter;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewModelTest {
    @Test
    void notifiesListenersEvenWhenTheSameStateIsSetAgain() {
        final ViewModel<String> model = new ViewModel<>("Example", "");
        final List<Object> seen = new ArrayList<>();
        model.addPropertyChangeListener(event -> seen.add(event.getNewValue()));

        model.setState("Email or password is incorrect.");
        model.setState("Email or password is incorrect.");

        assertEquals(List.of("Email or password is incorrect.", "Email or password is incorrect."), seen);
        assertEquals("Email or password is incorrect.", model.getState());
    }
}
