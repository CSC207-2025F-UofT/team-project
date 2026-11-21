package interface_adapter.map;

import org.junit.jupiter.api.Test;
import use_case.load_map.LoadMapOutputData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Checks display data, failure recovery and view notifications without Swing. */
class MapPresenterTest {
    private final MapViewModel viewModel = new MapViewModel();
    private final MapPresenter presenter = new MapPresenter(viewModel);

    @Test
    void startsWithoutAMapOrAnError() {
        MapState state = viewModel.getState();

        assertFalse(state.hasMap());
        assertEquals(0, state.getRowCount());
        assertEquals(0, state.getColumnCount());
        assertEquals(0, state.getWidthInPixels());
        assertEquals(0, state.getHeightInPixels());
        assertEquals("", state.getErrorMessage());
    }

    @Test
    void publishesTheCompleteMapBeforeNotifyingListeners() {
        List<PropertyChangeEvent> events = new ArrayList<>();
        viewModel.addPropertyChangeListener(event -> {
            assertSame(viewModel.getState(), event.getNewValue());
            events.add(event);
        });

        presenter.prepareSuccessView(new LoadMapOutputData(new int[][] {{1, 0, 1}, {0, 1, 0}}, 32));

        MapState state = viewModel.getState();
        assertTrue(state.hasMap());
        assertEquals(3, state.getColumnCount());
        assertEquals(2, state.getRowCount());
        assertEquals(32, state.getTileSize());
        assertEquals(96, state.getWidthInPixels());
        assertEquals(64, state.getHeightInPixels());
        assertEquals(0, state.getTileId(2, 1));
        assertArrayEquals(new int[] {1, 0, 1}, state.getTileIds()[0]);
        assertEquals("", state.getErrorMessage());
        assertEquals(1, events.size());
        assertEquals("state", events.get(0).getPropertyName());
        assertSame(viewModel, events.get(0).getSource());
    }

    @Test
    void presentsAnInitialFailureWithoutInventingAMap() {
        List<PropertyChangeEvent> events = new ArrayList<>();
        viewModel.addPropertyChangeListener(event -> events.add(event));

        presenter.prepareFailView("Map resource not found: maps/missing.txt");

        assertFalse(viewModel.getState().hasMap());
        assertEquals("Unable to load map: Map resource not found: maps/missing.txt",
                viewModel.getState().getErrorMessage());
        assertEquals(1, events.size());
    }

    @Test
    void preservesThePreviousMapAndErrorLocationWhenAnotherLoadFails() {
        presenter.prepareSuccessView(new LoadMapOutputData(new int[][] {{42, 0}}, 16));
        MapState previous = viewModel.getState();

        presenter.prepareFailView("Map 'bad.txt', line 4, column 2: Unknown tile ID: 9.");

        MapState state = viewModel.getState();
        assertTrue(state.hasMap());
        assertArrayEquals(new int[] {42, 0}, state.getTileIds()[0]);
        assertEquals(16, state.getTileSize());
        assertEquals("Unable to load map: Map 'bad.txt', line 4, column 2: Unknown tile ID: 9.",
                state.getErrorMessage());
        assertEquals("", previous.getErrorMessage());
    }

    @Test
    void clearsAnEarlierErrorWhenANewMapLoads() {
        presenter.prepareSuccessView(new LoadMapOutputData(new int[][] {{1, 0}}, 32));
        presenter.prepareFailView("Map unavailable.");

        presenter.prepareSuccessView(new LoadMapOutputData(new int[][] {{42}, {0}}, 16));

        MapState state = viewModel.getState();
        assertEquals("", state.getErrorMessage());
        assertEquals(1, state.getColumnCount());
        assertEquals(2, state.getRowCount());
        assertEquals(42, state.getTileId(0, 0));
        assertEquals(16, state.getTileSize());
    }

    @Test
    void notifiesForRepeatedFailuresAndSupportsRemovingAListener() {
        List<PropertyChangeEvent> events = new ArrayList<>();
        PropertyChangeListener listener = event -> events.add(event);
        viewModel.addPropertyChangeListener(listener);

        presenter.prepareFailView("Map unavailable.");
        presenter.prepareFailView("Map unavailable.");
        viewModel.removePropertyChangeListener(listener);
        presenter.prepareFailView("Another error.");

        assertEquals(2, events.size());
        assertEquals("Unable to load map: Another error.", viewModel.getState().getErrorMessage());
    }

    @Test
    void keepsMapStateImmutableWhenSuppliedOrReturnedArraysChange() {
        int[][] supplied = {{1, 0}, {0, 1}};
        MapState state = new MapState(supplied, 32, "");
        supplied[0][0] = 99;
        supplied[1] = new int[] {99};
        int[][] retrieved = state.getTileIds();
        retrieved[0] = new int[] {99};
        retrieved[1][0] = 99;

        assertArrayEquals(new int[] {1, 0}, state.getTileIds()[0]);
        assertArrayEquals(new int[] {0, 1}, state.getTileIds()[1]);
    }
}
