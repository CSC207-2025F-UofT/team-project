package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SampleTerrainsTest {

    @Test
    void hillsHaveHighAndLowGround() {
        Terrain hills = SampleTerrains.hills();
        assertEquals(48, hills.getRows());
        assertEquals(14, hills.getHeight(16, 14), 1e-9);
        assertTrue(hills.getHeight(22, 26) < 0, "valley dips below ground");
    }

    @Test
    void cityHasFlatStreetsAndBlocks() {
        Terrain city = SampleTerrains.city();
        assertEquals(0, city.getHeight(2, 2), 1e-9);
        assertEquals(12, city.getHeight(8, 8), 1e-9);
    }
}
