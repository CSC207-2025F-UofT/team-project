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
        model.addListener(() -> seen.add(model.getActiveView()));

        new SwitchViewController(model).switchTo("gallery");

        assertEquals("gallery", model.getActiveView());
        assertEquals(List.of("gallery"), seen);
    }
}
