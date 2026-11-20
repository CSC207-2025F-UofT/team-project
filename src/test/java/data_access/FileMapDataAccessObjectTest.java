package data_access;

import entity.DirtTileType;
import entity.TileMap;
import entity.TileType;
import entity.TileTypeRegistry;
import entity.WaterTileType;
import org.junit.jupiter.api.Test;
import use_case.load_map.MapDataAccessInterface;
import use_case.load_map.MapLoadingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Checks classpath loading and the map format, including useful failure locations. */
class FileMapDataAccessObjectTest {
    private final TileType water = new WaterTileType();
    private final TileType dirt = new DirtTileType();
    private final TileTypeRegistry registry = new TileTypeRegistry(List.of(water, dirt));
    private final FileMapDataAccessObject reader = new FileMapDataAccessObject(registry, 32);

    @Test
    void loadsAClasspathMapThroughTheUseCaseInterface() throws MapLoadingException {
        MapDataAccessInterface maps = reader;

        TileMap map = maps.load("small-map.txt");

        assertEquals(3, map.getColumnCount());
        assertEquals(2, map.getRowCount());
        assertEquals(96, map.getWidthInPixels());
        assertEquals(64, map.getHeightInPixels());
        assertSame(dirt, map.getTile(0, 0));
        assertSame(water, map.tileAtPixel(32, 0));
        assertFalse(map.isWalkable(0, 32));
        assertTrue(map.isWalkable(32, 32));
    }

    @Test
    void parsesNumbersIntoSharedTerrainObjects() throws IOException, MapLoadingException {
        TileMap map = parse("1 0 1\n0 1 0\n");

        assertSame(dirt, map.getTile(0, 0));
        assertSame(dirt, map.getTile(2, 0));
        assertSame(water, map.getTile(1, 0));
        assertSame(water, map.getTile(2, 1));
    }

    @Test
    void acceptsCommentsWhitespaceAndWindowsLineEndings() throws IOException, MapLoadingException {
        TileMap map = parse("  # terrain: eau et terre\r\n\r\n  1  \t0  \r\n\t\r\n # another comment\r\n0\t1\r\n");

        assertEquals(2, map.getRowCount());
        assertEquals(2, map.getColumnCount());
        assertSame(water, map.getTile(1, 0));
        assertSame(dirt, map.getTile(1, 1));
    }

    @Test
    void reportsTheFirstMissingColumnOfAShortRow() {
        MapLoadingException error = assertThrows(MapLoadingException.class,
                () -> parse("1 0 1\n# comment\n1 0\n"));

        assertTrue(error.getMessage().contains("line 3, column 3"));
        assertTrue(error.getMessage().contains("Expected 3 tiles but found 2"));
    }

    @Test
    void reportsTheFirstExtraColumnOfALongRow() {
        MapLoadingException error = assertThrows(MapLoadingException.class,
                () -> parse("1 0\n0 1 0\n"));

        assertTrue(error.getMessage().contains("line 2, column 3"));
        assertTrue(error.getMessage().contains("Expected 2 tiles but found 3"));
    }

    @Test
    void reportsUnknownIdsUsingOriginalFileLineNumbers() {
        MapLoadingException error = assertThrows(MapLoadingException.class,
                () -> parse("# header\n1 0\n\n  0   9\n"));

        assertTrue(error.getMessage().contains("sample.txt"));
        assertTrue(error.getMessage().contains("line 4, column 2"));
        assertTrue(error.getMessage().contains("Unknown tile ID: 9"));
    }

    @Test
    void reportsNonNumericIdsWithTheirLocation() {
        MapLoadingException error = assertThrows(MapLoadingException.class,
                () -> parse("# header\n\n1 0\n0 dirt\n"));

        assertTrue(error.getMessage().contains("line 4, column 2"));
        assertTrue(error.getMessage().contains("Invalid integer tile ID: 'dirt'"));
    }

    @Test
    void reportsNumbersOutsideTheIntegerRange() {
        MapLoadingException error = assertThrows(MapLoadingException.class,
                () -> parse("1 2147483648\n"));

        assertTrue(error.getMessage().contains("line 1, column 2"));
        assertTrue(error.getMessage().contains("Invalid integer tile ID: '2147483648'"));
    }

    @Test
    void rejectsAnUnregisteredNegativeId() {
        MapLoadingException error = assertThrows(MapLoadingException.class, () -> parse("1 -1\n"));

        assertTrue(error.getMessage().contains("line 1, column 2"));
        assertTrue(error.getMessage().contains("Unknown tile ID: -1"));
    }

    @Test
    void rejectsMapsWithoutTiles() {
        for (String text : new String[] {"", " \t\r\n", "# comment\n\n  # another comment\n"}) {
            MapLoadingException error = assertThrows(MapLoadingException.class, () -> parse(text));
            assertTrue(error.getMessage().contains("contains no tiles"));
        }
    }

    @Test
    void reportsAMissingResourceByName() {
        MapLoadingException error = assertThrows(MapLoadingException.class,
                () -> reader.load("missing-map.txt"));

        assertTrue(error.getMessage().contains("Map resource not found: maps/missing-map.txt"));
    }

    @Test
    void rejectsAnEmptyResource() {
        MapLoadingException error = assertThrows(MapLoadingException.class,
                () -> reader.load("empty-map.txt"));

        assertTrue(error.getMessage().contains("empty-map.txt"));
        assertTrue(error.getMessage().contains("contains no tiles"));
    }

    @Test
    void reportsLocationsForMalformedClasspathResources() {
        MapLoadingException error = assertThrows(MapLoadingException.class,
                () -> reader.load("invalid-map.txt"));

        assertTrue(error.getMessage().contains("invalid-map.txt"));
        assertTrue(error.getMessage().contains("line 4, column 2"));
        assertTrue(error.getMessage().contains("Unknown tile ID: 9"));
    }

    @Test
    void rejectsNullAndBlankMapNames() {
        for (String name : new String[] {null, "", " \t"}) {
            MapLoadingException error = assertThrows(MapLoadingException.class, () -> reader.load(name));
            assertTrue(error.getMessage().contains("Map name must not be blank"));
        }
    }

    @Test
    void usesTheConfiguredTileSize() throws IOException, MapLoadingException {
        FileMapDataAccessObject smallTiles = new FileMapDataAccessObject(registry, 16);
        try (BufferedReader input = new BufferedReader(new StringReader("1 0"))) {
            TileMap map = smallTiles.parse(input, "small-tiles.txt");

            assertEquals(32, map.getWidthInPixels());
            assertEquals(16, map.getHeightInPixels());
            assertSame(dirt, map.tileAtPixel(15, 0));
            assertSame(water, map.tileAtPixel(16, 0));
        }
    }

    @Test
    void rejectsInvalidReaderConfiguration() {
        assertThrows(IllegalArgumentException.class, () -> new FileMapDataAccessObject(null, 32));
        assertThrows(IllegalArgumentException.class, () -> new FileMapDataAccessObject(registry, 0));
        assertThrows(IllegalArgumentException.class, () -> new FileMapDataAccessObject(registry, -1));
    }

    @Test
    void resolvesAdditionalTerrainTypesThroughTheRegistry() throws IOException, MapLoadingException {
        TileType customTerrain = new TileType() {
            @Override
            public int getId() {
                return 42;
            }

            @Override
            public boolean isWalkable() {
                return true;
            }
        };
        TileTypeRegistry extended = new TileTypeRegistry(List.of(water, dirt, customTerrain));
        FileMapDataAccessObject extendedReader = new FileMapDataAccessObject(extended, 32);
        try (BufferedReader input = new BufferedReader(new StringReader("1 42 0"))) {
            TileMap map = extendedReader.parse(input, "custom.txt");

            assertSame(customTerrain, map.getTile(1, 0));
            assertTrue(map.isWalkable(32, 0));
        }
    }

    @Test
    void wrapsReadFailuresWithoutExposingAnIOException() throws IOException {
        IOException cause = new IOException("read failed");
        try (BufferedReader input = new BufferedReader(new StringReader("")) {
            @Override
            public String readLine() throws IOException {
                throw cause;
            }
        }) {
            MapLoadingException error = assertThrows(MapLoadingException.class,
                    () -> reader.parse(input, "unreadable.txt"));

            assertTrue(error.getMessage().contains("Could not read map 'unreadable.txt'"));
            assertSame(cause, error.getCause());
        }
    }

    @Test
    void wrapsInvalidMapDimensionsAsALoadingFailure() throws IOException {
        FileMapDataAccessObject hugeTiles = new FileMapDataAccessObject(registry, Integer.MAX_VALUE);
        try (BufferedReader input = new BufferedReader(new StringReader("1 0"))) {
            MapLoadingException error = assertThrows(MapLoadingException.class,
                    () -> hugeTiles.parse(input, "oversized.txt"));

            assertTrue(error.getMessage().contains("Invalid map 'oversized.txt'"));
            assertTrue(error.getMessage().contains("pixel dimensions"));
        }
    }

    private TileMap parse(String text) throws IOException, MapLoadingException {
        try (BufferedReader input = new BufferedReader(new StringReader(text))) {
            return reader.parse(input, "sample.txt");
        }
    }
}
