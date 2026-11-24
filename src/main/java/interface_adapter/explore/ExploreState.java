package interface_adapter.explore;

import java.util.ArrayList;
import java.util.List;

/**
 * Everything the Explore screen displays.
 *
 * <p>It contains only Strings, lists of Strings and ints, so the view never
 * depends on entities or use case classes. Presenters fill it in; ExploreView
 * reads it.</p>
 */
public class ExploreState {
    /** Shown in the title, e.g. "Floor 1". */
    private int floorNumber = 1;
    /** Shown in the title, e.g. "Armory". */
    private String roomName = "";
    /** The paragraph describing the room. */
    private String roomDescription = "";
    /** Names in the "Items in this room" list. */
    private List<String> roomItemNames = new ArrayList<>();
    /** Text shown when examining each item; same order as roomItemNames. */
    private List<String> roomItemDetails = new ArrayList<>();
    /** Directions with doors; used to enable or disable the compass buttons. */
    private List<String> exits = new ArrayList<>();
    /** Names in the inventory list. */
    private List<String> inventoryItemNames = new ArrayList<>();
    /** Maximum inventory size, for the "Inventory (2/3)" label. */
    private int inventoryCapacity;
    /** Message at the bottom of the screen, e.g. "You enter the Armory." */
    private String message = "";
    /** True if the message is a warning (shown in red instead of green). */
    private boolean error;

    /** Set when the inventory is full (extension 7a): the item the player wanted. */
    private String pendingItemName;

    /** @return the floor number */
    public int getFloorNumber() {
        return floorNumber;
    }

    /** @param floorNumber the floor number */
    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    /** @return the room's name */
    public String getRoomName() {
        return roomName;
    }

    /** @param roomName the room's name */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /** @return the room's description */
    public String getRoomDescription() {
        return roomDescription;
    }

    /** @param roomDescription the room's description */
    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    /** @return names of the items in the room */
    public List<String> getRoomItemNames() {
        return roomItemNames;
    }

    /** @param roomItemNames names of the items in the room */
    public void setRoomItemNames(List<String> roomItemNames) {
        this.roomItemNames = roomItemNames;
    }

    /** @return a readable description of each room item, in the same order as getRoomItemNames() */
    public List<String> getRoomItemDetails() {
        return roomItemDetails;
    }

    /** @param roomItemDetails a readable description of each room item */
    public void setRoomItemDetails(List<String> roomItemDetails) {
        this.roomItemDetails = roomItemDetails;
    }

    /** @return directions with doors, e.g. ["NORTH", "EAST"] */
    public List<String> getExits() {
        return exits;
    }

    /** @param exits directions with doors */
    public void setExits(List<String> exits) {
        this.exits = exits;
    }

    /** @return names of the carried items */
    public List<String> getInventoryItemNames() {
        return inventoryItemNames;
    }

    /** @param inventoryItemNames names of the carried items */
    public void setInventoryItemNames(List<String> inventoryItemNames) {
        this.inventoryItemNames = inventoryItemNames;
    }

    /** @return maximum inventory size */
    public int getInventoryCapacity() {
        return inventoryCapacity;
    }

    /** @param inventoryCapacity maximum inventory size */
    public void setInventoryCapacity(int inventoryCapacity) {
        this.inventoryCapacity = inventoryCapacity;
    }

    /** @return the message to show the player */
    public String getMessage() {
        return message;
    }

    /** @param message the message to show the player */
    public void setMessage(String message) {
        this.message = message;
    }

    /** @return true if the message is a warning */
    public boolean isError() {
        return error;
    }

    /** @param error true if the message is a warning */
    public void setError(boolean error) {
        this.error = error;
    }

    /** @return the item waiting to be picked up when the inventory is full, or null */
    public String getPendingItemName() {
        return pendingItemName;
    }

    /** @param pendingItemName the item waiting to be picked up, or null */
    public void setPendingItemName(String pendingItemName) {
        this.pendingItemName = pendingItemName;
    }
}
