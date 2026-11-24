package ViewManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JPanel;
import java.awt.CardLayout;

import static org.junit.jupiter.api.Assertions.*;

public class ViewManagerTest {

    private JPanel views;
    private JPanel panelA;
    private JPanel panelB;
    private ViewManagerModel model;

    @BeforeEach
    void setUp() {
        CardLayout layout = new CardLayout();
        views = new JPanel(layout);
        panelA = new JPanel();
        panelB = new JPanel();
        views.add(panelA, "a");
        views.add(panelB, "b");
        model = new ViewManagerModel();
        new ViewManager(views, layout, model);
    }

    @Test
    void viewChangedShowsTheCurrentView() {
        model.setState("b");
        model.notifyViewChanged();

        assertTrue(panelB.isVisible());
        assertFalse(panelA.isVisible());
    }

    @Test
    void canSwitchBackAndForth() {
        model.setState("b");
        model.notifyViewChanged();
        model.setState("a");
        model.notifyViewChanged();

        assertTrue(panelA.isVisible());
        assertFalse(panelB.isVisible());
    }

    @Test
    void settingStateWithoutNotifyDoesNotSwitchView() {
        model.setState("b");

        assertTrue(panelA.isVisible());
        assertFalse(panelB.isVisible());
    }
}
