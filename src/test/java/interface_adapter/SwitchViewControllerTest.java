package interface_adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SwitchViewControllerTest {

    @Test
    void switchToChangesTheActiveViewAndNotifies() {
        final ViewManagerModel model = new ViewManagerModel();
        final List<Object> seen = new ArrayList<>();
        model.addPropertyChangeListener(event -> seen.add(event.getNewValue()));

        new SwitchViewController(model).switchTo("gallery");

        assertEquals("gallery", model.getActiveView());
        assertEquals(List.of("gallery"), seen);
    }
}
