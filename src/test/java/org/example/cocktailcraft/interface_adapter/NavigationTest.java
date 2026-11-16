package org.example.cocktailcraft.interface_adapter;

import org.example.cocktailcraft.view.ViewManager;
import org.junit.jupiter.api.Test;

import java.awt.CardLayout;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NavigationTest {
    @Test
    void navigationNotifiesAndShowsRequestedCardOnEdt() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            ViewManagerModel model = new ViewManagerModel();
            CardLayout layout = new CardLayout();
            JPanel views = new JPanel(layout);
            JPanel home = new JPanel();
            JPanel profile = new JPanel();
            views.add(home, "home");
            views.add(profile, "profile");
            new ViewManager(views, layout, model);
            NavigationController navigation = new NavigationController(model);
            navigation.show("profile");
            assertEquals("profile", model.getCurrentViewName());
            assertTrue(profile.isVisible());
            assertFalse(home.isVisible());
            navigation.show("home");
            assertTrue(home.isVisible());
            assertFalse(profile.isVisible());
        });
    }

    @Test
    void stateListenersCanBeRemoved() {
        ViewModel<MessageState> model = new ViewModel<MessageState>(new MessageState()) { };
        AtomicInteger notifications = new AtomicInteger();
        StateChangeListener listener = notifications::incrementAndGet;
        model.addListener(listener);
        model.getState().setMessage("Changed");
        model.notifyStateChanged();
        assertEquals(1, notifications.get());
        model.removeListener(listener);
        model.notifyStateChanged();
        assertEquals(1, notifications.get());
    }
}
