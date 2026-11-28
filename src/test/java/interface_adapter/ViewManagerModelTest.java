package interface_adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ViewManagerModelTest {

    @Test
    void startsWithNoActiveView() {
        assertNull(new ViewManagerModel().getActiveView());
    }

    @Test
    void changingViewNotifiesListener() {
        final ViewManagerModel model = new ViewManagerModel();
        final List<String> seen = new ArrayList<>();
        model.addListener(() -> seen.add(model.getActiveView()));

        model.setActiveView("gallery");

        assertEquals("gallery", model.getActiveView());
        assertEquals(List.of("gallery"), seen);
    }

    @Test
    void setStateAloneDoesNotNotify() {
        final ViewManagerModel model = new ViewManagerModel();
        final List<String> seen = new ArrayList<>();
        model.addListener(() -> seen.add(model.getActiveView()));
        final ViewManagerState state = new ViewManagerState();
        state.setActiveView("login");

        model.setState(state);

        assertEquals("login", model.getActiveView());
        assertEquals(0, seen.size());
    }

    @Test
    void notifyStateChangedAnnouncesTheChosenState() {
        final ViewManagerModel model = new ViewManagerModel();
        final List<String> seen = new ArrayList<>();
        model.addListener(() -> seen.add(model.getActiveView()));
        final ViewManagerState state = new ViewManagerState();
        state.setActiveView("login");

        model.setState(state);
        model.notifyStateChanged();

        assertEquals(List.of("login"), seen);
    }
}
