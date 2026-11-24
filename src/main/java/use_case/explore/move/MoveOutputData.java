package use_case.explore.move;

import use_case.explore.ItemData;

import java.util.List;

/**
 * Output of the Move use case: everything needed to describe the room
 * the character is now standing in (Use Case 2, step 4).
 *
 * <p>It only holds simple values, so the presenter never touches entities.</p>
 */
public class MoveOutputData {
    /** The floor the character is on. */
    private final int floorNumber;
    /** The new room's name. */
    private final String roomName;
    /** The new room's description. */
    private final String roomDescription;
    /** Items lying in the new room. */
    private final List<ItemData> roomItems;
    /** Directions with doors, e.g. ["NORTH", "EAST"]. */
    private final List<String> exits;
    /** What the player is carrying, so the inventory panel stays up to date. */
    private final List<ItemData> inventoryItems;
    /** Maximum inventory size, for showing e.g. "2/3". */
    private final int inventoryCapacity;
    /** Name of a living monster in the room, or null if there is none. */
    private final String monsterName;

    /**
     * Creates the output data.
     *
     * @param floorNumber       the floor number
     * @param roomName          the room's name
     * @param roomDescription   the room's description
     * @param roomItems         items in the room
     * @param exits             directions that have doors
     * @param inventoryItems    items the player carries
     * @param inventoryCapacity maximum inventory size
     * @param monsterName       the monster's name, or null
     */
    public MoveOutputData(int floorNumber, String roomName, String roomDescription,
                          List<ItemData> roomItems, List<String> exits,
                          List<ItemData> inventoryItems, int inventoryCapacity,
                          String monsterName) {
        this.floorNumber = floorNumber;
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.roomItems = roomItems;
        this.exits = exits;
        this.inventoryItems = inventoryItems;
        this.inventoryCapacity = inventoryCapacity;
        this.monsterName = monsterName;
    }

    /** @return the floor number */
    public int getFloorNumber() {
        return floorNumber;
    }

    /** @return the room's name */
    public String getRoomName() {
        return roomName;
    }

    /** @return the room's description */
    public String getRoomDescription() {
        return roomDescription;
    }

    /** @return items in the room */
    public List<ItemData> getRoomItems() {
        return roomItems;
    }

    /** @return directions that have doors */
    public List<String> getExits() {
        return exits;
    }

    /** @return items the player carries */
    public List<ItemData> getInventoryItems() {
        return inventoryItems;
    }

    /** @return maximum inventory size */
    public int getInventoryCapacity() {
        return inventoryCapacity;
    }

    /** @return the name of the monster in the room, or null if there is none */
    public String getMonsterName() {
        return monsterName;
    }
}
