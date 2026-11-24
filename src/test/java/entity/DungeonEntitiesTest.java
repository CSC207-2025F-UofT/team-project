package entity;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the entities used by Use Case 2: Room, Inventory, Dungeon and Direction.
 */
class DungeonEntitiesTest {

    @Test
    void connectIsTwoWay() {
        Room a = new Room("A", "");
        Room b = new Room("B", "");
        a.connect(Direction.EAST, b);
        assertSame(b, a.getExit(Direction.EAST));
        assertSame(a, b.getExit(Direction.WEST));
        assertNull(a.getExit(Direction.NORTH));
    }

    @Test
    void inventoryRespectsCapacity() {
        Inventory inv = new Inventory(1);
        Item potion = new Item("Potion", "", ItemType.POTION, 5);
        assertTrue(inv.addItem(potion));
        assertTrue(inv.isFull());
        assertFalse(inv.addItem(new Item("Sword", "", ItemType.WEAPON, 5)));
        assertTrue(inv.removeItem(potion));
        assertEquals(0, inv.size());
        assertThrows(IllegalArgumentException.class, () -> new Inventory(0));
    }

    @Test
    void dungeonMoveAndFloorFinished() {
        Room a = new Room("A", "");
        Room b = new Room("B", "");
        a.connect(Direction.NORTH, b);
        Monster goblin = new Monster("Goblin", "goblin", 5, 15, new Stats(8, 14, 10, 10, 8, 8));
        b.setMonster(goblin);
        Dungeon dungeon = new Dungeon(2, Arrays.asList(a, b), a);

        assertFalse(dungeon.moveTo(Direction.SOUTH));
        assertSame(a, dungeon.getCurrentRoom());
        assertTrue(dungeon.moveTo(Direction.NORTH));
        assertSame(b, dungeon.getCurrentRoom());

        assertFalse(dungeon.isFloorFinished());
        goblin.takeDamage(5);
        assertFalse(b.monsterPresent());
        assertTrue(dungeon.isFloorFinished());
        assertEquals(2, dungeon.getFloorNumber());
    }

    @Test
    void startingRoomMustBelongToDungeon() {
        Room a = new Room("A", "");
        Room other = new Room("Other", "");
        assertThrows(IllegalArgumentException.class,
                () -> new Dungeon(1, Collections.singletonList(a), other));
    }

    @Test
    void directionOpposites() {
        assertEquals(Direction.SOUTH, Direction.NORTH.opposite());
        assertEquals(Direction.WEST, Direction.EAST.opposite());
        assertEquals("North", Direction.NORTH.displayName());
    }
}
