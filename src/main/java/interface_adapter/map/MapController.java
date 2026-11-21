package interface_adapter.map;

import use_case.load_map.LoadMapInputBoundary;
import use_case.load_map.LoadMapInputData;

/** Wraps a map name in a request and passes it to the load-map input boundary. */
public final class MapController {
    private final LoadMapInputBoundary loadMap;

    public MapController(LoadMapInputBoundary loadMap) {
        this.loadMap = loadMap;
    }

    public void load(String mapName) {
        loadMap.execute(new LoadMapInputData(mapName));
    }
}
