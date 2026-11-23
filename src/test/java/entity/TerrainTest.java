package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TerrainTest {

    private static final double DELTA = 1e-9;

    // 3 cols x 2 rows, 2 m apart. Height rises 1 m per column and 10 m per row.
    private static Terrain ramp() {
        return new Terrain("ramp", 2.0, new double[][] {
                {0, 1, 2},
                {10, 11, 12}
        });
    }

    @Test
    void storesFields() {
        Terrain terrain = ramp();
        assertEquals("ramp", terrain.getName());
        assertEquals(2, terrain.getRows());
        assertEquals(3, terrain.getCols());
        assertEquals(2.0, terrain.getCellSize(), DELTA);
        assertEquals(11, terrain.getHeight(1, 1), DELTA);
    }

    @Test
    void widthAndDepthAreInMetres() {
        Terrain terrain = ramp();
        assertEquals(4.0, terrain.width(), DELTA);
        assertEquals(2.0, terrain.depth(), DELTA);
    }

    @Test
    void copiesInputHeights() {
        double[][] heights = {{0, 0}, {0, 0}};
        Terrain terrain = new Terrain("flat", 1.0, heights);
        heights[0][0] = 99;
        assertEquals(0, terrain.getHeight(0, 0), DELTA);
    }

    @Test
    void getHeightsReturnsCopy() {
        Terrain terrain = ramp();
        terrain.getHeights()[0][0] = 99;
        assertEquals(0, terrain.getHeight(0, 0), DELTA);
    }

    @Test
    void heightAtGridPointMatchesStoredHeight() {
        assertEquals(12, ramp().heightAt(4, 2), DELTA);
    }

    @Test
    void heightAtBlendsBetweenGridPoints() {
        // Halfway along x in the first cell and halfway along y: (0 + 1 + 10 + 11) / 4.
        assertEquals(5.5, ramp().heightAt(1, 1), DELTA);
    }

    @Test
    void heightAtClampsOutsideGrid() {
        Terrain terrain = ramp();
        assertEquals(0, terrain.heightAt(-5, -5), DELTA);
        assertEquals(12, terrain.heightAt(100, 100), DELTA);
    }

    @Test
    void slopeAtPointsUphill() {
        Vector3D slope = ramp().slopeAt(1, 1);
        assertEquals(0.5, slope.getX(), DELTA);
        assertEquals(5.0, slope.getY(), DELTA);
        assertEquals(0, slope.getZ(), DELTA);
    }

    @Test
    void isInsideIncludesEdges() {
        Terrain terrain = ramp();
        assertTrue(terrain.isInside(0, 0));
        assertTrue(terrain.isInside(4, 2));
        assertFalse(terrain.isInside(4.1, 1));
        assertFalse(terrain.isInside(1, -0.1));
    }

    @Test
    void rejectsBadInput() {
        assertThrows(IllegalArgumentException.class,
                () -> new Terrain("x", 0, new double[][] {{0, 0}, {0, 0}}));
        assertThrows(IllegalArgumentException.class,
                () -> new Terrain("x", 1, new double[][] {{0, 0}}));
        assertThrows(IllegalArgumentException.class,
                () -> new Terrain("x", 1, new double[][] {{0, 0}, {0}}));
    }
}
