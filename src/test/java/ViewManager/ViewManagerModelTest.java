package ViewManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ViewManagerModelTest {

    private ViewManagerModel model;

    @BeforeEach
    void setUp() {
        model = new ViewManagerModel();
    }

    @Test
    void initialViewNameIsEmpty() {
        assertEquals("", model.getCurrentViewName());
    }

    @Test
    void setStateUpdatesCurrentViewName() {
        model.setState("log in");

        assertEquals("log in", model.getCurrentViewName());
    }

    @Test
    void setStateDoesNotNotifyListeners() {
        int[] calls = {0};
        model.addListener(() -> calls[0]++);

        model.setState("log in");

        assertEquals(0, calls[0]);
    }

    @Test
    void notifyViewChangedCallsEveryListener() {
        int[] first = {0};
        int[] second = {0};
        model.addListener(() -> first[0]++);
        model.addListener(() -> second[0]++);

        model.notifyViewChanged();

        assertEquals(1, first[0]);
        assertEquals(1, second[0]);
    }

    @Test
    void notifyViewChangedCallsListenerEachTime() {
        int[] calls = {0};
        model.addListener(() -> calls[0]++);

        model.notifyViewChanged();
        model.notifyViewChanged();

        assertEquals(2, calls[0]);
    }

    @Test
    void notifyWithNoListenersDoesNothing() {
        assertDoesNotThrow(() -> model.notifyViewChanged());
    }
}
