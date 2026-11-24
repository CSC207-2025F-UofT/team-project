package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One floor of the dungeon: a set of connected rooms and the room the
 * character is currently standing in.
 */
public class Dungeon {
    /** Which floor this is (1 = top). Used for the leaderboard in Use Case 4. */
    private final int floorNumber;
    /** Every room on this floor. */
    private final List<Room> rooms;
    /** Where the character is standing right now. */
    private Room currentRoom;

    /**
     * Creates a floor.
     *
     * @param floorNumber  the floor's number
     * @param rooms        all rooms on the floor (already connected to each other)
     * @param startingRoom where the character starts; must be one of the rooms
     * @throws IllegalArgumentException if startingRoom is not in rooms
     */
    public Dungeon(int floorNumber, List<Room> rooms, Room startingRoom) {
        // Guard against starting the character somewhere that isn't on this floor.
        if (!rooms.contains(startingRoom)) {
            throw new IllegalArgumentException("Starting room must be one of the dungeon's rooms");
        }
        this.floorNumber = floorNumber;
        // Copy the list so later changes to the caller's list don't affect the dungeon.
        this.rooms = new ArrayList<>(rooms);
        this.currentRoom = startingRoom;
    }

    /**
     * Moves the character to the room in the given direction (Use Case 2, step 3).
     *
     * @param direction direction to move
     * @return true if the move happened, false if there is no exit that way
     */
    public boolean moveTo(Direction direction) {
        Room next = currentRoom.getExit(direction);
        if (next == null) {
            // No door that way: stay in the current room (extension 2a).
            return false;
        }
        currentRoom = next;
        return true;
    }

    /**
     * A floor is finished once every monster on it has been defeated.
     *
     * @return true if no room on this floor has a living monster
     */
    public boolean isFloorFinished() {
        for (Room room : rooms) {
            if (room.monsterPresent()) {
                return false;
            }
        }
        return true;
    }

    /** @return this floor's number */
    public int getFloorNumber() {
        return floorNumber;
    }

    /** @return the room the character is in */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Puts the character in a specific room. Used by Load Game (Use Case 4)
     * to restore the character's position.
     *
     * @param room the room to move to; must be on this floor
     * @throws IllegalArgumentException if the room is not on this floor
     */
    public void setCurrentRoom(Room room) {
        if (!rooms.contains(room)) {
            throw new IllegalArgumentException("Room is not on this floor");
        }
        this.currentRoom = room;
    }

    /** @return a read-only list of every room on this floor */
    public List<Room> getRooms() {
        return Collections.unmodifiableList(rooms);
    }
}
