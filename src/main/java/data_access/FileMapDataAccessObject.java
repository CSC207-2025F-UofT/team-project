package data_access;

import entity.TileMap;
import entity.TileType;
import entity.TileTypeRegistry;
import use_case.load_map.MapDataAccessInterface;
import use_case.load_map.MapLoadingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** Reads UTF-8 tile maps from classpath resources under maps/. */
public final class FileMapDataAccessObject implements MapDataAccessInterface {
    private final TileTypeRegistry tileTypes;
    private final int tileSize;

    /**
     * Configures how stored tile IDs are resolved and how large tiles are in the world.
     *
     * @param tileTypes registry of the supported terrain types
     * @param tileSize width and height of each tile in pixels
     * @throws IllegalArgumentException if the registry is null or tileSize is not positive
     */
    public FileMapDataAccessObject(TileTypeRegistry tileTypes, int tileSize) {
        if (tileTypes == null) {
            throw new IllegalArgumentException("Tile type registry must not be null.");
        }
        if (tileSize <= 0) {
            throw new IllegalArgumentException("Tile size must be positive.");
        }
        this.tileTypes = tileTypes;
        this.tileSize = tileSize;
    }

    /**
     * Loads a resource such as maps/level1.txt by passing the name level1.txt.
     * The resource is closed on both success and failure.
     */
    @Override
    public TileMap load(String mapName) throws MapLoadingException {
        if (mapName == null || mapName.isBlank()) {
            throw new MapLoadingException("Map name must not be blank.");
        }
        InputStream stream = FileMapDataAccessObject.class.getResourceAsStream("/maps/" + mapName);
        if (stream == null) {
            throw new MapLoadingException("Map resource not found: maps/" + mapName);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            return parse(reader, mapName);
        } catch (IOException error) {
            throw new MapLoadingException("Could not read map '" + mapName + "'.", error);
        }
    }

    /** Parses rows from a caller-owned reader; the caller is responsible for closing it. */
    TileMap parse(BufferedReader reader, String mapName) throws MapLoadingException {
        List<TileType[]> rows = new ArrayList<>();
        int columnCount = 0;
        int lineNumber = 0;

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }

                String[] ids = trimmed.split("\\s+");
                if (rows.isEmpty()) {
                    columnCount = ids.length;
                } else if (ids.length != columnCount) {
                    int column = Math.min(ids.length, columnCount) + 1;
                    throw new MapLoadingException(location(mapName, lineNumber, column)
                            + "Expected " + columnCount + " tiles but found " + ids.length + ".");
                }

                TileType[] row = new TileType[columnCount];
                for (int col = 0; col < columnCount; col++) {
                    row[col] = parseTile(ids[col], mapName, lineNumber, col + 1);
                }
                rows.add(row);
            }

            if (rows.isEmpty()) {
                throw new MapLoadingException("Map '" + mapName + "' contains no tiles.");
            }
            return new TileMap(rows.toArray(new TileType[rows.size()][]), tileSize);
        } catch (IOException error) {
            throw new MapLoadingException("Could not read map '" + mapName + "'.", error);
        } catch (IllegalArgumentException error) {
            throw new MapLoadingException("Invalid map '" + mapName + "': " + error.getMessage(), error);
        }
    }

    private TileType parseTile(String token, String mapName, int line, int column) throws MapLoadingException {
        int id;
        try {
            id = Integer.parseInt(token);
        } catch (NumberFormatException error) {
            throw new MapLoadingException(location(mapName, line, column)
                    + "Invalid integer tile ID: '" + token + "'.", error);
        }

        try {
            return tileTypes.fromId(id);
        } catch (IllegalArgumentException error) {
            throw new MapLoadingException(location(mapName, line, column)
                    + "Unknown tile ID: " + id + ".", error);
        }
    }

    /** Error columns count tiles from one, rather than characters or spaces. */
    private String location(String mapName, int line, int column) {
        return "Map '" + mapName + "', line " + line + ", column " + column + ": ";
    }
}
