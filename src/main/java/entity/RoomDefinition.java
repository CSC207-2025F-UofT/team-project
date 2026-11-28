package entity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Immutable story configuration. */
public final class RoomDefinition {
    public final String id;
    public final String name;
    public final String description;
    public final List<InteractableDefinition> interactables;
    public final Optional<String> nextRoomId;
    public final ItemLockDefinition doorLock;
    public final TriviaLockDefinition exitChallenge;

    public RoomDefinition(String id,
                          String name,
                          String description,
                          List<InteractableDefinition> interactables,
                          Optional<String> nextRoomId,
                          ItemLockDefinition doorLock,
                          TriviaLockDefinition exitChallenge) {
        this.id = TemplateText.require(id, "id");
        this.name = TemplateText.require(name, "name");
        this.description = TemplateText.require(description, "description");
        this.interactables = List.copyOf(interactables);
        this.nextRoomId = Objects.requireNonNull(nextRoomId, "nextRoomId");
        this.doorLock = Objects.requireNonNull(doorLock, "doorLock");
        this.exitChallenge = Objects.requireNonNull(exitChallenge, "exitChallenge");
    }
}
