package use_case.load_map;

/** An immutable request identifying the map to load. */
public final class LoadMapInputData {
    private final String mapName;

    /** Stores the name unchanged; the interactor handles missing or blank names. */
    public LoadMapInputData(String mapName) {
        this.mapName = mapName;
    }

    public String getMapName() {
        return mapName;
    }
}
