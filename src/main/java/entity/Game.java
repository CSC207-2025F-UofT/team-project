package entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/** Domain command coordinator. Commands run serially through the application dispatcher. */
public final class Game {
    private final String templateId;
    private final int templateVersion;
    private final Map<String, String> generatedValues;
    private final String name;
    private final String background;
    private final Map<String, Room> rooms;
    private final Inventory inventory = new Inventory();
    private String currentRoomId;
    private GameStatus status = GameStatus.ACTIVE;

    /** New-game construction enters the starting room before the game can be published. */
    public Game(String name, String background, Map<String, Room> rooms, String startingRoomId) {
        this(name, background, rooms, startingRoomId, name, 1, Map.of());
    }

    public Game(String name, String background, Map<String, Room> rooms, String startingRoomId,
                String templateId, int templateVersion, Map<String, String> generatedValues) {
        this.templateId = templateId;
        this.templateVersion = templateVersion;
        this.generatedValues = Map.copyOf(generatedValues);
        if (name == null || name.isBlank() || background == null || background.isBlank()) {
            throw new IllegalArgumentException("Game name and background are required");
        }
        this.name = name;
        this.background = background;
        this.rooms = new LinkedHashMap<>(Objects.requireNonNull(rooms, "rooms"));
        validateWorld(startingRoomId);
        enterRoom(startingRoomId);
    }

    public String templateId() { return templateId; }
    public int templateVersion() { return templateVersion; }
    public Map<String, String> generatedValues() { return generatedValues; }
    public String name() { return name; }
    public String background() { return background; }
    public GameStatus status() { return status; }
    public Room currentRoom() { return rooms.get(currentRoomId); }
    public List<Collectable> inventoryItems() { return inventory.items(); }
    public boolean canMove() { return status == GameStatus.ACTIVE && currentRoom().canLeave(); }

    /** Validate the whole command before state/completion/location changes begin. */
    public DomainResult interact(String objectId, String actionId, Optional<String> itemId) {
        try {
            requireActive();
            if (objectId == null || objectId.isBlank() || actionId == null || actionId.isBlank()) {
                throw new DomainException(ResultCode.INVALID_INPUT, "Object and action IDs are required.");
            }
            Room room = currentRoom();
            Interactable object = room.findInteractable(objectId).orElseThrow(() ->
                    new DomainException(ResultCode.INVALID_TARGET, "Object is not in the current room: " + objectId));
            Action action = object.requireAction(actionId);
            // Completed one-shot operations ignore a stale selection naming an unheld tool.
            if (!action.isRepeatable() && object.isActionCompleted(actionId)) {
                return new DomainResult(ResultCode.ACTION_COMPLETED, false, "That action is already completed.");
            }
            if (itemId == null || itemId.filter(String::isBlank).isPresent()) {
                throw new DomainException(ResultCode.INVALID_INPUT, "Selected item must be an optional nonblank ID.");
            }
            Optional<Collectable> tool = itemId.map(inventory::require);
            ActionPlan plan = room.prepareInteraction(objectId, actionId, tool);
            preflightAcquisition(room, plan.appearingItemIds());
            DomainResult applied = room.applyInteraction(plan);
            List<Collectable> obtained = acquireItems(room, plan.appearingItemIds());
            boolean changed = applied.changed() || !obtained.isEmpty();
            return new DomainResult(changed ? ResultCode.SUCCESS : ResultCode.NO_CHANGE, changed,
                    withObtained(applied.resultText(), obtained));
        } catch (DomainException exception) {
            return failure(exception);
        }
    }

    public DomainResult useItems(String lockId) {
        try {
            requireActive();
            ItemLock lock = currentRoom().doorLock();
            requireLockId(lock, lockId);
            return lock.tryUnlock(inventory);
        } catch (DomainException exception) { return failure(exception); }
    }

    public Optional<Trivia> triviaFor(String lockId) {
        TriviaLock lock = currentRoom().exitChallenge();
        requireLockId(lock, lockId);
        return lock.getTrivia();
    }

    public void installTrivia(String lockId, Trivia question) {
        requireActive();
        TriviaLock lock = currentRoom().exitChallenge();
        requireLockId(lock, lockId);
        lock.initializeTrivia(question);
    }

    public DomainResult submitAnswer(String lockId, String answer) {
        try {
            requireActive();
            TriviaLock lock = currentRoom().exitChallenge();
            requireLockId(lock, lockId);
            return lock.submitAnswer(answer);
        } catch (DomainException exception) { return failure(exception); }
    }

    public DomainResult move() {
        try {
            requireActive();
            if (!currentRoom().doorLock().isUnlocked()) {
                return new DomainResult(ResultCode.DOOR_LOCKED, false, "Unlock the door before leaving.");
            }
            if (!currentRoom().exitChallenge().isUnlocked()) {
                return new DomainResult(ResultCode.TRIVIA_LOCKED, false, "Pass the exit question before leaving.");
            }
            if (currentRoom().isFinal()) {
                status = GameStatus.ESCAPED;
                return new DomainResult(ResultCode.SUCCESS, true, "You escaped!");
            }
            return enterRoom(currentRoom().nextRoomId().orElseThrow());
        } catch (DomainException exception) { return failure(exception); }
    }

    /** Read-only metadata lookup, including items still in other rooms. */
    public List<Collectable> itemDetails(Set<String> itemIds) {
        Objects.requireNonNull(itemIds, "itemIds");
        Map<String, Collectable> all = new LinkedHashMap<>();
        for (Room room : rooms.values()) for (Collectable item : room.allCollectables()) all.put(item.id(), item);
        for (Collectable item : inventory.items()) all.put(item.id(), item);
        List<Collectable> result = new ArrayList<>();
        for (String id : itemIds) {
            Collectable item = all.get(id);
            if (item == null) throw new DomainException(ResultCode.INVALID_TARGET, "Unknown item ID: " + id);
            result.add(item);
        }
        return List.copyOf(result);
    }

    private DomainResult enterRoom(String roomId) {
        Room destination = rooms.get(roomId);
        if (destination == null) throw new DomainException(ResultCode.INVALID_TARGET, "Unknown room: " + roomId);
        Set<String> visible = destination.visibleCollectables().stream().map(Collectable::id)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        preflightAcquisition(destination, visible);
        currentRoomId = roomId;
        List<Collectable> obtained = acquireItems(destination, visible);
        return new DomainResult(ResultCode.SUCCESS, true, withObtained(destination.describe(), obtained));
    }

    private void preflightAcquisition(Room room, Set<String> itemIds) {
        for (String itemId : itemIds) {
            if (!inventory.contains(itemId) && room.findCollectable(itemId).isEmpty()) {
                throw new DomainException(ResultCode.INVALID_ACQUISITION, "Appearing item is not in the current room: " + itemId);
            }
        }
    }

    private List<Collectable> acquireItems(Room room, Set<String> itemIds) {
        List<Collectable> obtained = new ArrayList<>();
        for (String itemId : itemIds) {
            if (!inventory.contains(itemId)) {
                Collectable item = room.takeCollectable(itemId);
                item.reveal();
                inventory.add(item);
                obtained.add(item);
            }
        }
        return List.copyOf(obtained);
    }

    private void requireActive() {
        if (status != GameStatus.ACTIVE) throw new DomainException(ResultCode.GAME_ESCAPED, "This game is already escaped.");
    }

    private void requireLockId(Lock lock, String requestedId) {
        if (!lock.id().equals(requestedId)) throw new DomainException(ResultCode.INVALID_TARGET, "Lock is not accessible here: " + requestedId);
    }

    private DomainResult failure(DomainException exception) {
        return new DomainResult(exception.code(), false, exception.getMessage());
    }

    private static String withObtained(String message, List<Collectable> obtained) {
        if (obtained.isEmpty()) return message;
        return message + "\nObtained: " + obtained.stream().map(Collectable::name).collect(Collectors.joining(", ")) + ".";
    }

    private void validateWorld(String startingRoomId) {
        if (rooms.isEmpty() || !rooms.containsKey(startingRoomId)) throw new IllegalArgumentException("A valid starting room is required");
        Set<String> globalIds = new LinkedHashSet<>();
        for (Map.Entry<String, Room> entry : rooms.entrySet()) {
            Room room = Objects.requireNonNull(entry.getValue(), "room");
            if (!room.id().equals(entry.getKey())) throw new IllegalArgumentException("Room map keys must match IDs");
            unique(globalIds, room.id());
            for (Interactable object : room.interactables()) unique(globalIds, object.id());
            for (Collectable item : room.allCollectables()) unique(globalIds, item.id());
            for (Lock lock : room.availableLocks()) unique(globalIds, lock.id());
        }
        Set<String> visited = new LinkedHashSet<>();
        Optional<String> next = Optional.of(startingRoomId);
        while (next.isPresent()) {
            Room room = rooms.get(next.get());
            if (room == null || !visited.add(next.get())) throw new IllegalArgumentException("Room chain must be finite and reference existing rooms");
            next = room.nextRoomId();
        }
        if (visited.size() != rooms.size()) throw new IllegalArgumentException("Room chain must cover every room");
        for (Room room : rooms.values()) {
            for (String requiredId : room.doorLock().requiredItemIds()) {
                boolean found = rooms.values().stream().flatMap(value -> value.allCollectables().stream())
                        .anyMatch(item -> item.id().equals(requiredId));
                if (!found) throw new IllegalArgumentException("Unknown required item: " + requiredId);
            }
        }
    }

    private static void unique(Set<String> ids, String id) {
        if (!ids.add(id)) throw new IllegalArgumentException("Duplicate world ID: " + id);
    }
}
