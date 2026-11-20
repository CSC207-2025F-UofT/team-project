package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Checks the map's shape, coordinate boundaries, and protection against outside changes. */
class TileMapTest {
    private final TileType water = new WaterTileType();
    private final TileType dirt = new DirtTileType();

    @Test
    void reportsGridAndPixelDimensions() {
        TileMap map = createMap();

        assertEquals(3, map.getColumnCount());
        assertEquals(2, map.getRowCount());
        assertEquals(32, map.getTileSize());
        assertEquals(96, map.getWidthInPixels());
        assertEquals(64, map.getHeightInPixels());
    }

    @Test
    void acceptsASingleTileMap() {
        TileMap map = new TileMap(new TileType[][] {{dirt}}, 32);

        assertEquals(32, map.getWidthInPixels());
        assertEquals(32, map.getHeightInPixels());
        assertTrue(map.isWalkable(31.999, 31.999));
    }

    @Test
    void looksUpTilesByColumnThenRow() {
        TileMap map = createMap();

        assertSame(dirt, map.getTile(0, 0));
        assertSame(water, map.getTile(1, 0));
        assertSame(dirt, map.getTile(2, 0));
        assertSame(dirt, map.getTile(1, 1));
        assertSame(water, map.getTile(2, 1));
    }

    @Test
    void looksUpFractionalPixelPositions() {
        TileMap map = createMap();

        assertSame(dirt, map.tileAtPixel(0, 0));
        assertSame(dirt, map.tileAtPixel(31.999, 31.999));
        assertSame(water, map.tileAtPixel(32.5, 12.75));
        assertSame(water, map.tileAtPixel(95.999, 63.999));
    }

    @Test
    void tileBoundariesSelectTheNextColumnOrRow() {
        TileMap map = createMap();

        assertSame(water, map.tileAtPixel(32, 0));
        assertSame(water, map.tileAtPixel(0, 32));
        assertSame(dirt, map.tileAtPixel(32, 32));
        assertSame(dirt, map.tileAtPixel(64, 0));
    }

    @Test
    void usesTheSuppliedTileSizeForLookups() {
        TileMap map = new TileMap(new TileType[][] {{dirt, water}}, 16);

        assertEquals(32, map.getWidthInPixels());
        assertEquals(16, map.getHeightInPixels());
        assertSame(dirt, map.tileAtPixel(15.999, 0));
        assertSame(water, map.tileAtPixel(16, 0));
    }

    @Test
    void reportsWalkabilityAtPixelPositions() {
        TileMap map = createMap();

        assertTrue(map.isWalkable(0, 0));
        assertTrue(map.isWalkable(31.999, 31.999));
        assertFalse(map.isWalkable(32, 0));
        assertFalse(map.isWalkable(0, 32));
        assertTrue(map.isWalkable(32, 32));
    }

    @Test
    void rejectsTileIndicesOutsideTheGrid() {
        TileMap map = createMap();
        int[][] positions = {{-1, 0}, {0, -1}, {3, 0}, {0, 2}};

        for (int[] position : positions) {
            assertThrows(IndexOutOfBoundsException.class, () -> map.getTile(position[0], position[1]));
        }
    }

    @Test
    void rejectsInvalidPixelLookupsAndTreatsThemAsBlocked() {
        TileMap map = createMap();
        double[][] positions = {
                {-0.001, 0}, {0, -0.001}, {96, 0}, {0, 64},
                {Double.NaN, 0}, {0, Double.NaN},
                {Double.POSITIVE_INFINITY, 0}, {0, Double.POSITIVE_INFINITY},
                {Double.NEGATIVE_INFINITY, 0}, {0, Double.NEGATIVE_INFINITY}
        };

        for (double[] position : positions) {
            assertThrows(IndexOutOfBoundsException.class, () -> map.tileAtPixel(position[0], position[1]));
            assertFalse(map.isWalkable(position[0], position[1]));
        }
    }

    @Test
    void copiesEachRowOfTheSuppliedGrid() {
        TileType[][] tiles = {{dirt, water}, {water, dirt}};
        TileMap map = new TileMap(tiles, 32);

        tiles[0][0] = water;
        tiles[1][1] = water;

        assertSame(dirt, map.getTile(0, 0));
        assertSame(dirt, map.getTile(1, 1));
    }

    @Test
    void replacingASuppliedRowDoesNotChangeTheMap() {
        TileType[][] tiles = {{dirt, water}};
        TileMap map = new TileMap(tiles, 32);

        tiles[0] = new TileType[] {water};

        assertEquals(2, map.getColumnCount());
        assertSame(dirt, map.getTile(0, 0));
        assertSame(water, map.getTile(1, 0));
    }

    @Test
    void rejectsNullAndEmptyGrids() {
        assertThrows(IllegalArgumentException.class, () -> new TileMap(null, 32));
        assertThrows(IllegalArgumentException.class, () -> new TileMap(new TileType[0][], 32));
        assertThrows(IllegalArgumentException.class, () -> new TileMap(new TileType[][] {{}}, 32));
    }

    @Test
    void rejectsNullRows() {
        assertThrows(IllegalArgumentException.class, () -> new TileMap(new TileType[][] {null}, 32));
        assertThrows(IllegalArgumentException.class,
                () -> new TileMap(new TileType[][] {{dirt}, null}, 32));
    }

    @Test
    void rejectsUnevenRows() {
        TileType[][] tiles = {{dirt, water}, {dirt}};

        assertThrows(IllegalArgumentException.class, () -> new TileMap(tiles, 32));
    }

    @Test
    void rejectsNullTiles() {
        TileType[][] tiles = {{dirt, null}};

        assertThrows(IllegalArgumentException.class, () -> new TileMap(tiles, 32));
    }

    @Test
    void rejectsNonPositiveTileSizes() {
        TileType[][] tiles = {{dirt}};

        assertThrows(IllegalArgumentException.class, () -> new TileMap(tiles, 0));
        assertThrows(IllegalArgumentException.class, () -> new TileMap(tiles, -1));
    }

    @Test
    void rejectsPixelDimensionsThatWouldOverflow() {
        TileType[][] wideMap = {{dirt, dirt}};
        TileType[][] tallMap = {{dirt}, {dirt}};

        assertThrows(IllegalArgumentException.class, () -> new TileMap(wideMap, Integer.MAX_VALUE));
        assertThrows(IllegalArgumentException.class, () -> new TileMap(tallMap, Integer.MAX_VALUE));
    }

    private TileMap createMap() {
        return new TileMap(new TileType[][] {
                {dirt, water, dirt},
                {water, dirt, water}
        }, 32);
    }
}
