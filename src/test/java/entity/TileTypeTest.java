package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Checks the terrain IDs and walking rules shared by map consumers. */
class TileTypeTest {
    @Test
    void waterHasIdZeroAndBlocksWalking() {
        TileType water = new WaterTileType();

        assertEquals(0, water.getId());
        assertFalse(water.isWalkable());
    }

    @Test
    void dirtHasIdOneAndAllowsWalking() {
        TileType dirt = new DirtTileType();

        assertEquals(1, dirt.getId());
        assertTrue(dirt.isWalkable());
    }
}
