package use_case;

import entity.*;
import java.util.*;

/** Checks references and explores room-local action sequences in forward path order. */
final class TemplateValidator {
    private TemplateValidator() { }

    static void validate(StoryTemplate story) {
        Map<String, RoomDefinition> rooms = new HashMap<>();
        Map<String, String> owners = new HashMap<>();
        Set<String> ids = new HashSet<>();
        for (RoomDefinition room : story.rooms) {
            unique(ids, room.id);
            rooms.put(room.id, room);
            unique(ids, room.doorLock.id);
            unique(ids, room.exitChallenge.id);
            for (InteractableDefinition obj : room.interactables) unique(ids, obj.id);
        }
        for (CollectableDefinition item : story.items) {
            unique(ids, item.id);
            require(rooms.containsKey(item.roomId), "Unknown item owner: " + item.roomId);
            owners.put(item.id, item.roomId);
        }
        Optional<String> next = Optional.of(story.startingRoomId);
        Set<String> visited = new HashSet<>();
        Set<Set<String>> inventories = Set.of(Set.of());
        while (next.isPresent()) {
            RoomDefinition room = rooms.get(next.get());
            require(room != null && visited.add(next.get()), "Broken or cyclic room chain at " + next.get());
            validateRoom(room, owners);
            Set<String> visible = new HashSet<>();
            for (CollectableDefinition item : story.items) {
                if (item.roomId.equals(room.id) && item.initiallyRevealed) visible.add(item.id);
            }
            inventories = explore(room, inventories, visible);
            next = room.nextRoomId;
        }
        require(visited.size() == rooms.size(), "Unreachable rooms in story");
    }

    private static void validateRoom(RoomDefinition room, Map<String, String> owners) {
        require(!room.doorLock.requiredItemIds.isEmpty() && owners.keySet().containsAll(room.doorLock.requiredItemIds),
                "Missing or unknown door items in " + room.id);
        Map<String, InteractableDefinition> objects = new HashMap<>();
        for (InteractableDefinition obj : room.interactables) objects.put(obj.id, obj);
        for (InteractableDefinition obj : room.interactables) {
            Set<String> actions = new HashSet<>();
            for (ActionDefinition a : obj.actions) {
                unique(actions, a.id);
                a.requiredItemId.ifPresent(id -> require(owners.containsKey(id), "Unknown tool: " + id));
                for (String id : a.appearingItemIds) require(room.id.equals(owners.get(id)), "Foreign appearance: " + id);
                for (StateClause c : a.preconditions) validateClause(c, objects);
                for (StateClause c : a.effects) validateClause(c, objects);
            }
        }
    }

    private static void validateClause(StateClause c, Map<String, InteractableDefinition> objects) {
        InteractableDefinition obj = objects.get(c.objectId);
        require(obj != null && obj.allowedStateValues.getOrDefault(c.key, Set.of()).contains(c.value),
                "Invalid state reference: " + c.objectId + "." + c.key + "=" + c.value);
    }

    // Keep alternative inventories separate: mutually exclusive actions cannot supply a joint requirement.
    private static Set<Set<String>> explore(RoomDefinition room, Set<Set<String>> incoming, Set<String> visible) {
        Map<List<String>, String> initial = new HashMap<>();
        Set<ActionDefinition> unseen = new HashSet<>();
        for (InteractableDefinition obj : room.interactables) {
            obj.initialState.forEach((key, value) -> initial.put(List.of(obj.id, key), value));
            unseen.addAll(obj.actions);
        }
        Deque<Position> pending = new ArrayDeque<>();
        for (Set<String> inventory : incoming) {
            Set<String> items = new HashSet<>(inventory);
            items.addAll(visible);
            pending.add(new Position(initial, items, Set.of()));
        }
        Set<List<?>> visited = new HashSet<>();
        Set<Set<String>> exits = new HashSet<>();
        while (!pending.isEmpty()) {
            Position pos = pending.removeFirst();
            if (!visited.add(List.of(pos.state, pos.items, pos.completed))) continue;
            if (pos.items.containsAll(room.doorLock.requiredItemIds)) exits.add(pos.items);
            for (InteractableDefinition obj : room.interactables) {
                for (ActionDefinition action : obj.actions) {
                    if (pos.canPerform(action)) {
                        unseen.remove(action);
                        pending.add(pos.after(action));
                    }
                }
            }
        }
        require(!exits.isEmpty(), "Door items cannot be obtained together in " + room.id);
        require(unseen.isEmpty(), "Unreachable action in " + room.id);
        return exits;
    }

    private static final class Position {
        final Map<List<String>, String> state;
        final Set<String> items;
        final Set<ActionDefinition> completed;

        Position(Map<List<String>, String> state, Set<String> items, Set<ActionDefinition> completed) {
            this.state = state;
            this.items = items;
            this.completed = completed;
        }

        boolean canPerform(ActionDefinition a) {
            return !completed.contains(a) && a.requiredItemId.map(items::contains).orElse(true)
                    && a.preconditions.stream().allMatch(c -> c.value.equals(state.get(List.of(c.objectId, c.key))));
        }

        Position after(ActionDefinition a) {
            Map<List<String>, String> next = new HashMap<>(state);
            for (StateClause c : a.effects) next.put(List.of(c.objectId, c.key), c.value);
            Set<String> held = new HashSet<>(items);
            held.addAll(a.appearingItemIds);
            Set<ActionDefinition> done = new HashSet<>(completed);
            if (!a.repeatable) done.add(a);
            return new Position(next, held, done);
        }
    }

    private static void unique(Set<String> seen, String id) {
        require(seen.add(id), "Duplicate ID: " + id);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }
}
