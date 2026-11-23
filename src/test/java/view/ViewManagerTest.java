package view;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.CardLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.Test;

import interface_adapter.ViewManagerModel;

class ViewManagerTest {

    @Test
    void switchesVisibleCardWhenModelChanges() {
        final CardLayout layout = new CardLayout();
        final JPanel views = new JPanel(layout);
        final JLabel first = new JLabel("first");
        final JLabel second = new JLabel("second");
        views.add(first, "first");
        views.add(second, "second");
        final ViewManagerModel model = new ViewManagerModel();
        new ViewManager(views, layout, model);

        model.setActiveView("second");

        assertTrue(second.isVisible());
        assertFalse(first.isVisible());
    }
}
