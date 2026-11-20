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
        final List<Object> seen = new ArrayList<>();
        model.addPropertyChangeListener(event -> seen.add(event.getNewValue()));

        model.setActiveView("gallery");

        assertEquals("gallery", model.getActiveView());
        assertEquals(List.of("gallery"), seen);
    }

    @Test
    void settingSameViewTwiceNotifiesOnce() {
        final ViewManagerModel model = new ViewManagerModel();
        final List<Object> seen = new ArrayList<>();
        model.addPropertyChangeListener(event -> seen.add(event.getNewValue()));

        model.setActiveView("gallery");
        model.setActiveView("gallery");

        assertEquals(1, seen.size());
    }
}
