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
    private final Exit exit;
    private final Map<String, Interactable> interactables;

    public Room(String id, String name, String description, Exit exit, Map<String, Interactable> interactables) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.description = Objects.requireNonNull(description, "description");
        this.exit = Objects.requireNonNull(exit, "exit");

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

    /** Returns the ID of this room's exit. */
    public String getExit() {
        return exit.getId();
    }

    /** Returns the interactable keys in the order supplied to the constructor. */
    public List<String> getInteractable() {
        return List.copyOf(interactables.keySet());
    }

    public Boolean canLeave() {
        return exit.canLeave();
    }

    /** Runs the exit's departure behavior. Game owns the current-room transition. */
    public void exit() {
        if (!canLeave()) {
            throw new IllegalStateException("Cannot leave room " + id);
        }
        exit.exit();
    }
}
