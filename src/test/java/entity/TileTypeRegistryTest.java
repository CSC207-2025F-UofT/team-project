package entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Checks terrain registration, ID validation, and support for additional implementations. */
class TileTypeRegistryTest {
    private final TileType water = new WaterTileType();
    private final TileType dirt = new DirtTileType();

    @Test
    void looksUpRegisteredObjectsByIdInsteadOfListPosition() {
        TileTypeRegistry registry = new TileTypeRegistry(List.of(dirt, water));

        assertSame(water, registry.fromId(0));
        assertSame(dirt, registry.fromId(1));
        assertSame(water, registry.fromId(0));
    }

    @Test
    void rejectsUnknownIdsWithTheIdInTheMessage() {
        TileTypeRegistry registry = new TileTypeRegistry(List.of(water, dirt));

        for (int id : new int[] {-1, 2, Integer.MAX_VALUE}) {
            IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                    () -> registry.fromId(id));
            assertTrue(error.getMessage().contains(Integer.toString(id)));
        }
    }

    @Test
    void rejectsDifferentObjectsWithTheSameId() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new TileTypeRegistry(List.of(dirt, new DirtTileType())));

        assertTrue(error.getMessage().contains("Duplicate tile id: 1"));
    }

    @Test
    void anotherImplementationWorksWithTheRegistryAndMap() {
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
        TileTypeRegistry registry = new TileTypeRegistry(List.of(water, dirt, customTerrain));
        TileMap map = new TileMap(new TileType[][] {{registry.fromId(42)}}, 32);

        assertSame(customTerrain, registry.fromId(42));
        assertSame(customTerrain, map.tileAtPixel(0, 0));
        assertTrue(map.isWalkable(0, 0));
    }

    @Test
    void changingTheSuppliedListDoesNotChangeRegistrations() {
        List<TileType> types = new ArrayList<>();
        types.add(water);
        TileTypeRegistry registry = new TileTypeRegistry(types);

        types.clear();
        types.add(dirt);

        assertSame(water, registry.fromId(0));
        assertThrows(IllegalArgumentException.class, () -> registry.fromId(1));
    }

    @Test
    void anEmptyRegistryReportsUnknownIds() {
        TileTypeRegistry registry = new TileTypeRegistry(List.of());

        assertThrows(IllegalArgumentException.class, () -> registry.fromId(0));
    }

    @Test
    void rejectsANullList() {
        assertThrows(IllegalArgumentException.class, () -> new TileTypeRegistry(null));
    }

    @Test
    void rejectsNullEntries() {
        assertThrows(IllegalArgumentException.class,
                () -> new TileTypeRegistry(Arrays.asList(water, null)));
    }
}
