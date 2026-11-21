package data_access;

import entity.DirtTileType;
import entity.TileMap;
import entity.TileTypeRegistry;
import entity.WaterTileType;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Verifies the real MVP arena resource through the production map reader. */
class ProductionMapTest {
    @Test
    void loadsTheScreenSizedArenaWithBothTerrainTypesAndAnOpenCentre() throws Exception {
        TileTypeRegistry registry = new TileTypeRegistry(List.of(new WaterTileType(), new DirtTileType()));
        TileMap map = new FileMapDataAccessObject(registry, 32).load("level1.txt");

        assertEquals(30, map.getColumnCount());
        assertEquals(20, map.getRowCount());
        assertEquals(960, map.getWidthInPixels());
        assertEquals(640, map.getHeightInPixels());
        Set<Integer> ids = new HashSet<>();
        for (int row = 0; row < map.getRowCount(); row++) {
            for (int col = 0; col < map.getColumnCount(); col++) {
                ids.add(map.getTile(col, row).getId());
            }
        }
        assertEquals(Set.of(0, 1), ids);
        assertTrue(map.isWalkable(456, 296));
        assertTrue(map.isWalkable(503, 343));
    }
}
