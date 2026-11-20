package use_case.load_map;

import entity.TileMap;

/** Supplies complete terrain maps to the map-loading use case. */
public interface MapDataAccessInterface {
    /**
     * Loads a map identified by name.
     *
     * @param mapName non-blank name identifying the requested map
     * @return a complete, valid map; never null
     * @throws MapLoadingException if the name is invalid or the map is unavailable or malformed
     */
    TileMap load(String mapName) throws MapLoadingException;
}
