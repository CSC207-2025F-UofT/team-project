package use_case.explore;

import data_access.InMemoryExploreDataAccessObject;
import entity.Dungeon;
import entity.Inventory;
import entity.Item;
import entity.ItemType;
import entity.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.explore.pick_up_item.PickUpItemInputData;
import use_case.explore.pick_up_item.PickUpItemInteractor;
import use_case.explore.pick_up_item.PickUpItemOutputBoundary;
import use_case.explore.pick_up_item.PickUpItemOutputData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for PickUpItemInteractor (Use Case 2, steps 5-8 and extension 7a).
 *
 * <p>Uses a room with a Longsword and a Potion, and an inventory that holds 2 items,
 * so the "inventory full" case is easy to set up.</p>
 */
class PickUpItemInteractorTest {
    private Room room;
    private Inventory inventory;
    private InMemoryExploreDataAccessObject dataAccess;
    private Item sword;

    @BeforeEach
    void setUp() {
        room = new Room("Armory", "Weapons everywhere.");
        sword = new Item("Longsword", "A sword.", ItemType.WEAPON, 8);
        room.addItem(sword);
        room.addItem(new Item("Potion", "Heals.", ItemType.POTION, 7));
        Dungeon dungeon = new Dungeon(1, Collections.singletonList(room), room);
        inventory = new Inventory(2);
        dataAccess = new InMemoryExploreDataAccessObject(dungeon, inventory);
    }

    /**
     * Presenter where every method fails the test. Each test overrides only the
     * one method it expects to be called.
     */
    private abstract static class StrictPresenter implements PickUpItemOutputBoundary {
        @Override
        public void prepareSuccessView(PickUpItemOutputData outputData) {
            fail("Unexpected success view");
        }

        @Override
        public void prepareInventoryFullView(String itemName, List<String> inventoryItemNames) {
            fail("Unexpected inventory full view");
        }

        @Override
        public void prepareFailView(String errorMessage) {
            fail("Unexpected fail view: " + errorMessage);
        }
    }

    @Test
    void pickUpMovesItemFromRoomToInventory() {
        final boolean[] called = {false};
        PickUpItemOutputBoundary presenter = new StrictPresenter() {
            @Override
            public void prepareSuccessView(PickUpItemOutputData data) {
                assertEquals("Longsword", data.getPickedUpItemName());
                assertNull(data.getDiscardedItemName());
                assertEquals(1, data.getInventoryItems().size());
                assertEquals("Longsword", data.getInventoryItems().get(0).getName());
                assertEquals(1, data.getRoomItems().size());
                assertEquals("Potion", data.getRoomItems().get(0).getName());
                assertEquals(2, data.getInventoryCapacity());
                called[0] = true;
            }
        };

        new PickUpItemInteractor(dataAccess, presenter).execute(new PickUpItemInputData("Longsword"));

        assertTrue(called[0]);
        assertNotNull(inventory.findItem("Longsword"));
        assertNull(room.findItem("Longsword"));
    }

    @Test
    void fullInventoryLeavesItemInRoom() {
        inventory.addItem(new Item("Shield", "A shield.", ItemType.ARMOR, 2));
        inventory.addItem(new Item("Dagger", "A dagger.", ItemType.WEAPON, 4));
        final boolean[] called = {false};
        PickUpItemOutputBoundary presenter = new StrictPresenter() {
            @Override
            public void prepareInventoryFullView(String itemName, List<String> names) {
                assertEquals("Longsword", itemName);
                assertEquals(Arrays.asList("Shield", "Dagger"), names);
                called[0] = true;
            }
        };

        new PickUpItemInteractor(dataAccess, presenter).execute(new PickUpItemInputData("Longsword"));

        assertTrue(called[0]);
        assertNotNull(room.findItem("Longsword"));
        assertNull(inventory.findItem("Longsword"));
        assertEquals(2, inventory.size());
    }

    @Test
    void fullInventoryCanSwapItem() {
        inventory.addItem(new Item("Shield", "A shield.", ItemType.ARMOR, 2));
        inventory.addItem(new Item("Dagger", "A dagger.", ItemType.WEAPON, 4));
        final boolean[] called = {false};
        PickUpItemOutputBoundary presenter = new StrictPresenter() {
            @Override
            public void prepareSuccessView(PickUpItemOutputData data) {
                assertEquals("Longsword", data.getPickedUpItemName());
                assertEquals("Dagger", data.getDiscardedItemName());
                called[0] = true;
            }
        };

        new PickUpItemInteractor(dataAccess, presenter)
                .execute(new PickUpItemInputData("Longsword", "Dagger"));

        assertTrue(called[0]);
        assertNotNull(inventory.findItem("Longsword"));
        assertNull(inventory.findItem("Dagger"));
        assertNotNull(room.findItem("Dagger"), "Dropped item should be left in the room");
        assertNull(room.findItem("Longsword"));
        assertEquals(2, inventory.size());
    }

    @Test
    void discardIgnoredWhenInventoryHasSpace() {
        inventory.addItem(new Item("Dagger", "A dagger.", ItemType.WEAPON, 4));
        PickUpItemOutputBoundary presenter = new StrictPresenter() {
            @Override
            public void prepareSuccessView(PickUpItemOutputData data) {
                assertNull(data.getDiscardedItemName());
            }
        };

        new PickUpItemInteractor(dataAccess, presenter)
                .execute(new PickUpItemInputData("Longsword", "Dagger"));

        assertNotNull(inventory.findItem("Dagger"));
        assertNotNull(inventory.findItem("Longsword"));
    }

    @Test
    void discardingItemNotCarriedFails() {
        inventory.addItem(new Item("Shield", "A shield.", ItemType.ARMOR, 2));
        inventory.addItem(new Item("Dagger", "A dagger.", ItemType.WEAPON, 4));
        final boolean[] called = {false};
        PickUpItemOutputBoundary presenter = new StrictPresenter() {
            @Override
            public void prepareFailView(String errorMessage) {
                assertTrue(errorMessage.contains("Bow"));
                called[0] = true;
            }
        };

        new PickUpItemInteractor(dataAccess, presenter)
                .execute(new PickUpItemInputData("Longsword", "Bow"));

        assertTrue(called[0]);
        assertNotNull(room.findItem("Longsword"));
    }

    @Test
    void pickingUpMissingItemFails() {
        final boolean[] called = {false};
        PickUpItemOutputBoundary presenter = new StrictPresenter() {
            @Override
            public void prepareFailView(String errorMessage) {
                assertTrue(errorMessage.contains("Wand"));
                called[0] = true;
            }
        };

        new PickUpItemInteractor(dataAccess, presenter).execute(new PickUpItemInputData("Wand"));
        assertTrue(called[0]);
        assertEquals(0, inventory.size());
    }
}
