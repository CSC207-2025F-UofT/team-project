package interface_adapter.map;

import entity.DirtTileType;
import entity.TileMap;
import entity.TileType;
import entity.WaterTileType;
import org.junit.jupiter.api.Test;
import use_case.load_map.LoadMapInteractor;
import use_case.load_map.MapDataAccessInterface;
import use_case.load_map.MapLoadingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Connects the real controller, interactor and presenter using an in-memory map source. */
class LoadMapFlowTest {
    @Test
    void publishesSuccessFailureAndRecoveryThroughTheBoundaries() {
        Map<String, TileMap> maps = new HashMap<>();
        maps.put("first", new TileMap(new TileType[][] {{new DirtTileType(), new WaterTileType()}}, 32));
        maps.put("second", new TileMap(new TileType[][] {{new WaterTileType()}}, 16));
        MapDataAccessInterface dataAccess = name -> {
            TileMap map = maps.get(name);
            if (map == null) {
                throw new MapLoadingException("Map unavailable: " + name);
            }
            return map;
        };
        MapViewModel viewModel = new MapViewModel();
        List<MapState> displayedStates = new ArrayList<>();
        viewModel.addPropertyChangeListener(event -> displayedStates.add(viewModel.getState()));
        MapController controller = new MapController(
                new LoadMapInteractor(dataAccess, new MapPresenter(viewModel)));

        controller.load("");
        controller.load("first");
        controller.load("missing");
        controller.load("second");

        assertEquals(4, displayedStates.size());
        assertFalse(displayedStates.get(0).hasMap());
        assertEquals("Unable to load map: Map name must not be blank.",
                displayedStates.get(0).getErrorMessage());
        assertTrue(displayedStates.get(1).hasMap());
        assertEquals(64, displayedStates.get(1).getWidthInPixels());
        assertEquals("", displayedStates.get(1).getErrorMessage());
        assertEquals(64, displayedStates.get(2).getWidthInPixels());
        assertEquals("Unable to load map: Map unavailable: missing", displayedStates.get(2).getErrorMessage());
        assertEquals(16, displayedStates.get(3).getWidthInPixels());
        assertEquals(0, displayedStates.get(3).getTileId(0, 0));
        assertEquals("", displayedStates.get(3).getErrorMessage());
    }
}
