package use_case;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Immutable post-command display data, independent of widgets and saved answers. */
public final class GameOutputData {
    private final Optional<String> roomId;
    private final String title;
    private final String narrative;
    private final Map<String, String> objectDescriptions;
    private final List<ItemData> inventory;
    private final List<ActionData> actions;
    private final List<LockData> locks;
    private final Optional<TriviaData> activeTrivia;
    private final List<StorySummary> stories;
    private final String message;
    private final boolean canMove;
    private final boolean finalRoom;
    private final boolean escaped;

    public GameOutputData(Optional<String> roomId, String title, String narrative,
                          Map<String, String> objectDescriptions, List<ItemData> inventory,
                          List<ActionData> actions, List<LockData> locks,
                          Optional<TriviaData> activeTrivia, List<StorySummary> stories,
                          String message, boolean canMove, boolean finalRoom, boolean escaped) {
        this.roomId = Objects.requireNonNull(roomId, "roomId");
        this.title = Objects.requireNonNull(title, "title");
        this.narrative = Objects.requireNonNull(narrative, "narrative");
        LinkedHashMap<String, String> descriptions = new LinkedHashMap<>();
        objectDescriptions.forEach((id, description) -> descriptions.put(
                Objects.requireNonNull(id, "object ID"), Objects.requireNonNull(description, "description")));
        this.objectDescriptions = Collections.unmodifiableMap(descriptions);
        this.inventory = List.copyOf(inventory);
        this.actions = List.copyOf(actions);
        this.locks = List.copyOf(locks);
        this.activeTrivia = Objects.requireNonNull(activeTrivia, "activeTrivia");
        this.stories = List.copyOf(stories);
        this.message = Objects.requireNonNull(message, "message");
        this.canMove = canMove;
        this.finalRoom = finalRoom;
        this.escaped = escaped;
    }

    public Optional<String> roomId() { return roomId; }
    public String title() { return title; }
    public String narrative() { return narrative; }
    public Map<String, String> objectDescriptions() { return objectDescriptions; }
    public List<ItemData> inventory() { return inventory; }
    public List<ActionData> actions() { return actions; }
    public List<LockData> locks() { return locks; }
    public Optional<TriviaData> activeTrivia() { return activeTrivia; }
    public List<StorySummary> stories() { return stories; }
    public String message() { return message; }
    public boolean canMove() { return canMove; }
    public boolean finalRoom() { return finalRoom; }
    public boolean escaped() { return escaped; }
}
