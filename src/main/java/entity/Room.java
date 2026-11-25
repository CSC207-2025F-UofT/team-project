package entity;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Room {
    private final String id;
    private final String name;
    private final String description;
    private final TriviaLock triviaLock;
    private final ItemLock itemLock;
    private final String nextRoom;
    private final Map<String, Interactable> interactables;

    /** A null nextRoom marks the final room. */
    public Room(String id, String name, String description, TriviaLock triviaLock, ItemLock itemLock,
                String nextRoom, Map<String, Interactable> interactables) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.description = Objects.requireNonNull(description, "description");
        this.triviaLock = Objects.requireNonNull(triviaLock, "triviaLock");
        this.itemLock = Objects.requireNonNull(itemLock, "itemLock");
        this.nextRoom = nextRoom;

        Map<String, Interactable> copy = new LinkedHashMap<>();
        Objects.requireNonNull(interactables, "interactables").forEach((key, interactable) ->
                copy.put(Objects.requireNonNull(key, "interactable key"),
                        Objects.requireNonNull(interactable, "interactable")));
        this.interactables = Collections.unmodifiableMap(copy);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /** Returns the next room's ID, or null for the final room. */
    public String getExit() {
        return nextRoom;
    }

    /** Returns the interactable keys in the order supplied to the constructor. */
    public List<String> getInteractable() {
        return List.copyOf(interactables.keySet());
    }

    public Boolean canLeave() {
        return Boolean.TRUE.equals(triviaLock.isUnlocked()) && Boolean.TRUE.equals(itemLock.isUnlocked());
    }

    /** Checks departure before Game moves to the next room or ends the game. */
    public void exit() {
        if (!canLeave()) {
            throw new IllegalStateException("Cannot leave room " + id);
        }
    }

    public Boolean isFinal() {
        return nextRoom == null;
    }
}
