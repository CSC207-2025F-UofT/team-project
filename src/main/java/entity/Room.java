package entity;

import entity.state.ObjectState;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Room-local objects, remaining physical items and two independent exit gates. */
public final class Room {
    private final String id;
    private final String name;
    private final String description;
    private final TriviaLock triviaLock;
    private final ItemLock itemLock;
    private final Optional<String> nextRoomId;
    private final Map<String, Interactable> interactables;
    private final Map<String, Collectable> collectables;
    private final StateAccess stateAccess = new StateAccess();

    /** Compatibility constructor: a null successor identifies the final room. */
    public Room(String id, String name, String description, TriviaLock triviaLock, ItemLock itemLock,
                String nextRoom, Map<String, Interactable> interactables) {
        this(id, name, description, triviaLock, itemLock, Optional.ofNullable(nextRoom), interactables, Map.of());
    }

    public Room(String id, String name, String description, TriviaLock triviaLock, ItemLock itemLock,
                Optional<String> nextRoomId, Map<String, Interactable> interactables,
                Map<String, Collectable> collectables) {
        this.id = text(id, "room id");
        this.name = text(name, "room name");
        this.description = text(description, "room description");
        this.triviaLock = Objects.requireNonNull(triviaLock, "triviaLock");
        this.itemLock = Objects.requireNonNull(itemLock, "itemLock");
        this.nextRoomId = Objects.requireNonNull(nextRoomId, "nextRoomId");
        nextRoomId.ifPresent(value -> text(value, "successor id"));
        this.interactables = new LinkedHashMap<>(Objects.requireNonNull(interactables, "interactables"));
        this.collectables = new LinkedHashMap<>(Objects.requireNonNull(collectables, "collectables"));
        this.interactables.forEach((key, object) -> {
            if (object == null || !object.id().equals(key)) throw new IllegalArgumentException("Object map keys must match IDs");
        });
        this.collectables.forEach((key, item) -> {
            if (item == null || !item.id().equals(key)) throw new IllegalArgumentException("Item map keys must match IDs");
        });
    }

    public String id() { return id; }
    public String name() { return name; }
    public String describe() { return description; }
    public List<Interactable> interactables() { return List.copyOf(interactables.values()); }
    public Optional<Interactable> findInteractable(String objectId) { return Optional.ofNullable(interactables.get(objectId)); }
    public Optional<String> nextRoomId() { return nextRoomId; }
    public ItemLock doorLock() { return itemLock; }
    public TriviaLock exitChallenge() { return triviaLock; }
    public Boolean canLeave() { return itemLock.isUnlocked() && triviaLock.isUnlocked(); }
    public Boolean isFinal() { return nextRoomId.isEmpty(); }
    public List<Lock> availableLocks() { return List.of(itemLock, triviaLock); }

    public List<ActionOption> availableActions(Set<String> itemIds) {
        Objects.requireNonNull(itemIds, "itemIds");
        List<ActionOption> options = new ArrayList<>();
        for (Interactable object : interactables.values()) {
            for (Action action : object.actions()) {
                if ((!object.isActionCompleted(action.id()) || action.isRepeatable())
                        && action.isAvailable(stateAccess, itemIds)) {
                    options.add(new ActionOption(object.id(), action.id(), action.label(), action.requiresItem()));
                }
            }
        }
        return List.copyOf(options);
    }

    Optional<Collectable> findCollectable(String itemId) { return Optional.ofNullable(collectables.get(itemId)); }
    List<Collectable> visibleCollectables() {
        List<Collectable> visible = new ArrayList<>();
        for (Collectable item : collectables.values()) if (item.isRevealed()) visible.add(item);
        return List.copyOf(visible);
    }
    List<Collectable> allCollectables() { return List.copyOf(collectables.values()); }
    Collectable takeCollectable(String itemId) {
        Collectable item = collectables.remove(itemId);
        if (item == null) throw new DomainException(ResultCode.INVALID_ACQUISITION, "Item is not in this room: " + itemId);
        return item;
    }

    ActionPlan prepareInteraction(String objectId, String actionId, Optional<Collectable> item) {
        return requireObject(objectId).prepare(actionId, Objects.requireNonNull(item, "item"), stateAccess);
    }

    DomainResult applyInteraction(ActionPlan plan) {
        validatePlan(plan);
        Interactable owner = requireObject(plan.objectId());
        Map<String, Map<String, String>> before = objectValues();
        for (Effect effect : plan.effects()) effect.apply(stateAccess);
        boolean completed = owner.markCompleted(plan.actionId());
        boolean changed = completed || !before.equals(objectValues());
        return new DomainResult(changed ? ResultCode.SUCCESS : ResultCode.NO_CHANGE, changed, plan.resultText());
    }

    private void validatePlan(ActionPlan plan) {
        Objects.requireNonNull(plan, "plan");
        Interactable owner = requireObject(plan.objectId());
        Action action = owner.requireAction(plan.actionId());
        if (!plan.matches(owner, action)) throw new DomainException(ResultCode.INVALID_INPUT, "Action plan does not belong to this room");
        if (!action.isRepeatable() && owner.isActionCompleted(action.id())) {
            throw new DomainException(ResultCode.ACTION_COMPLETED, "That action is already completed.");
        }
        plan.validate(stateAccess);
    }

    private Map<String, Map<String, String>> objectValues() {
        Map<String, Map<String, String>> values = new LinkedHashMap<>();
        for (Interactable object : interactables.values()) {
            ObjectState state = object.snapshot();
            values.put(object.id(), state.values());
        }
        return values;
    }

    private Interactable requireObject(String objectId) {
        Interactable object = interactables.get(objectId);
        if (object == null) throw new DomainException(ResultCode.INVALID_TARGET, "Object is not in the current room: " + objectId);
        return object;
    }

    /** Effects receive this capability only during a validated room-local commit. */
    private final class StateAccess implements RoomReadState, RoomMutation {
        @Override public String objectState(String objectId, String key) { return requireObject(objectId).stateValue(key); }
        @Override public boolean acceptsObjectState(String objectId, String key, String value) {
            Interactable object = interactables.get(objectId);
            return object != null && object.acceptsStateValue(key, value);
        }
        @Override public void setObjectState(String objectId, String key, String value) {
            requireObject(objectId).setStateValue(key, value);
        }
    }

    public String getId() { return id(); }
    public String getName() { return name(); }
    public String getDescription() { return describe(); }
    public String getExit() { return nextRoomId.orElse(null); }
    public List<String> getInteractable() { return List.copyOf(interactables.keySet()); }
    public void exit() {
        if (!canLeave()) throw new IllegalStateException("Cannot leave room " + id);
    }

    private static String text(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " must not be blank");
        return value;
    }
}
