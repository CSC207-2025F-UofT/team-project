package entity;

import entity.TerrainFeature.Kind;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TerrainBuilderTest {

    private static final double DELTA = 1e-9;

    @Test
    void defaultsToFlatGridAtGroundLevel() {
        Terrain terrain = new TerrainBuilder("Flat").ground(3).build();
        assertEquals("Flat", terrain.getName());
        assertEquals(TerrainBuilder.DEFAULT_GRID_SIZE, terrain.getRows());
        assertEquals(TerrainBuilder.DEFAULT_GRID_SIZE, terrain.getCols());
        assertEquals(TerrainBuilder.DEFAULT_CELL_SIZE, terrain.getCellSize(), DELTA);
        assertEquals(3, terrain.getHeight(0, 0), DELTA);
        assertEquals(3, terrain.getHeight(47, 47), DELTA);
    }

    @Test
    void putsHillWhereAskedInGridCells() {
        Terrain terrain = new TerrainBuilder("Hill")
                .cellSize(5)
                .addFeature(new TerrainFeature(Kind.HILL, 12, 30, 8, 20, null))
                .build();
        // Feature x is a column and y is a row, whatever the cell size.
        assertEquals(20, terrain.getHeight(30, 12), DELTA);
        assertEquals(10, terrain.getHeight(30, 16), DELTA);
        assertEquals(0, terrain.getHeight(0, 0), DELTA);
        assertEquals(20, terrain.heightAt(12 * 5, 30 * 5), DELTA);
    }

    @Test
    void overlappingFeaturesAddToGround() {
        Terrain terrain = new TerrainBuilder("Mixed")
                .gridSize(10, 10)
                .ground(2)
                .addFeatures(List.of(
                        new TerrainFeature(Kind.BLOCK, 5, 5, 2, 10, null),
                        new TerrainFeature(Kind.VALLEY, 5, 5, 4, 4, null)))
                .build();
        assertEquals(2 + 10 - 4, terrain.getHeight(5, 5), DELTA);
        assertEquals(2, terrain.getHeight(0, 0), DELTA);
    }

    @Test
    void usesRequestedGridSize() {
        Terrain terrain = new TerrainBuilder("Small").gridSize(4, 6).build();
        assertEquals(4, terrain.getRows());
        assertEquals(6, terrain.getCols());
    }

    @Test
    void rejectsBadSettings() {
        TerrainBuilder builder = new TerrainBuilder("Bad");
        assertThrows(IllegalArgumentException.class, () -> builder.gridSize(1, 5));
        assertThrows(IllegalArgumentException.class, () -> builder.cellSize(0));
        assertThrows(NullPointerException.class, () -> builder.addFeature(null));
    }
}
