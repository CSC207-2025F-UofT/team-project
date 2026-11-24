package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * A single room of a dungeon floor. A room has a description, exits to
 * neighbouring rooms, zero or more items and possibly a monster.
 *
 * <p>Rooms are linked to each other directly (like nodes in a graph), so
 * "which room is north of here?" is just a map lookup.</p>
 */
public class Room {
    /** Short name, e.g. "Armory". */
    private final String name;
    /** Text shown when the player enters (Use Case 2, step 4). */
    private final String description;
    /** For each direction that has a door, the room on the other side. */
    private final Map<Direction, Room> exits = new EnumMap<>(Direction.class);
    /** Items lying on the floor. A list, because a room can hold several items. */
    private final List<Item> items = new ArrayList<>();
    /** The monster in this room, or null if there is none. */
    private Monster monster;

    /**
     * Creates an empty room with no exits.
     *
     * @param name        the room's name
     * @param description what the player sees on entering
     */
    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Connects this room to another room in both directions.
     * e.g. a.connect(NORTH, b) means b is north of a AND a is south of b,
     * so the player can always walk back the way they came.
     *
     * @param direction where the other room is, as seen from this room
     * @param other     the neighbouring room
     */
    public void connect(Direction direction, Room other) {
        exits.put(direction, other);
        other.exits.put(direction.opposite(), this);
    }

    /**
     * Looks up the room in a direction (Use Case 2, step 2).
     *
     * @param direction direction to look in
     * @return the neighbouring room, or null if there is no exit that way
     */
    public Room getExit(Direction direction) {
        return exits.get(direction);
    }

    /**
     * Lists the directions that have doors. Returns a copy so callers can't
     * change this room's exits by accident.
     *
     * @return the directions the player can move from here
     */
    public List<Direction> getExitDirections() {
        return new ArrayList<>(exits.keySet());
    }

    /**
     * Puts an item on the floor of this room. Used when building the dungeon,
     * and when the player drops an item to make space (extension 7a).
     *
     * @param item the item to add
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Removes an item from the room, e.g. after the player picks it up (step 7).
     *
     * @param item the item to remove
     * @return true if the item was in this room
     */
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    /**
     * Finds the first item in this room with the given name.
     *
     * @param itemName the item's name
     * @return the item, or null if there is none
     */
    public Item findItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equals(itemName)) {
                return item;
            }
        }
        return null;
    }

    /** @return a read-only view of the items in this room */
    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Checks for a monster (Use Case 2, extension 4a). A defeated monster
     * doesn't count, so the player can walk back through cleared rooms.
     *
     * @return true if a living monster is in this room
     */
    public boolean monsterPresent() {
        return monster != null && monster.isAlive();
    }

    /** @return the monster in this room, or null */
    public Monster getMonster() {
        return monster;
    }

    /**
     * Places a monster in this room.
     *
     * @param monster the monster, or null to remove it
     */
    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    /** @return the room's name */
    public String getName() {
        return name;
    }

    /** @return the room's description */
    public String getDescription() {
        return description;
    }
}
