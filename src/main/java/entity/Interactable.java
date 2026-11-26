package entity;

import entity.state.ObjectState;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** A room-owned object with immutable configuration and private runtime progress. */
public final class Interactable {
    private final String id;
    private final String name;
    private final Map<String, String> descriptionsByMode;
    private final Map<String, String> state;
    private final Map<String, Set<String>> allowedStateValues;
    private final Set<String> completedActionIds = new LinkedHashSet<>();
    private final Map<String, Action> actions;

    public Interactable(String id, String name, Map<String, String> descriptionsByMode,
                        Map<String, String> initialState, Map<String, Set<String>> allowedStateValues,
                        Map<String, Action> actions) {
        this.id = requireText(id, "id");
        this.name = requireText(name, "name");
        this.descriptionsByMode = copyTextMap(descriptionsByMode, "descriptionsByMode");
        this.state = new LinkedHashMap<>(copyTextMap(initialState, "initialState"));
        LinkedHashMap<String, Set<String>> allowedCopy = new LinkedHashMap<>();
        Objects.requireNonNull(allowedStateValues, "allowedStateValues").forEach((key, values) -> {
            LinkedHashSet<String> accepted = new LinkedHashSet<>();
            Objects.requireNonNull(values, "allowed values").forEach(value ->
                    accepted.add(requireText(value, "allowed value")));
            if (accepted.isEmpty()) {
                throw new IllegalArgumentException("Every state key needs at least one accepted value");
            }
            allowedCopy.put(requireText(key, "state key"), Collections.unmodifiableSet(accepted));
        });
        this.allowedStateValues = Collections.unmodifiableMap(allowedCopy);
        if (!this.state.keySet().equals(this.allowedStateValues.keySet())) {
            throw new IllegalArgumentException("Initial and allowed state keys must match");
        }
        this.state.forEach((key, value) -> {
            if (!acceptsStateValue(key, value)) {
                throw new IllegalArgumentException("Initial state value is not accepted: " + key);
            }
        });
        if (!this.state.containsKey("mode")
                || !this.descriptionsByMode.keySet().equals(this.allowedStateValues.get("mode"))) {
            throw new IllegalArgumentException("Every allowed mode needs exactly one description");
        }
        LinkedHashMap<String, Action> actionsCopy = new LinkedHashMap<>();
        Objects.requireNonNull(actions, "actions").forEach((key, action) -> {
            Objects.requireNonNull(action, "action");
            if (!requireText(key, "action key").equals(action.id())) {
                throw new IllegalArgumentException("Action map key must match its action ID");
            }
            actionsCopy.put(key, action);
        });
        this.actions = Collections.unmodifiableMap(actionsCopy);
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String describe() {
        return descriptionsByMode.get(state.get("mode"));
    }

    public List<Action> actions() {
        return List.copyOf(actions.values());
    }

    public boolean isActionCompleted(String actionId) {
        return completedActionIds.contains(actionId);
    }

    Action requireAction(String actionId) {
        Action action = actions.get(actionId);
        if (action == null) {
            throw new DomainException(ResultCode.INVALID_TARGET, "No such action on object " + id + ".");
        }
        return action;
    }

    String stateValue(String key) {
        String value = state.get(key);
        if (value == null) {
            throw new DomainException(ResultCode.INVALID_TARGET, "Unknown state key on object " + id + ".");
        }
        return value;
    }

    boolean acceptsStateValue(String key, String value) {
        Set<String> accepted = allowedStateValues.get(key);
        return accepted != null && accepted.contains(value);
    }

    void setStateValue(String key, String value) {
        if (!acceptsStateValue(key, value)) {
            throw new DomainException(ResultCode.INVALID_EFFECT, "Invalid state value on object " + id + ".");
        }
        state.put(key, value);
    }

    ActionPlan prepare(String actionId, Optional<Collectable> item, RoomReadState facts) {
        Action action = requireAction(actionId);
        if (!action.isRepeatable() && isActionCompleted(actionId)) {
            throw new DomainException(ResultCode.ACTION_COMPLETED, "That action has already been completed.");
        }
        return action.prepare(id, facts, item).forOwner(this);
    }

    boolean markCompleted(String actionId) {
        Action action = requireAction(actionId);
        return !action.isRepeatable() && completedActionIds.add(actionId);
    }

    /** A detached immutable value for the saving package; it exposes no mutation capability. */
    public ObjectState snapshot() {
        return new ObjectState(id, state, completedActionIds);
    }

    private static Map<String, String> copyTextMap(Map<String, String> source, String name) {
        LinkedHashMap<String, String> copy = new LinkedHashMap<>();
        Objects.requireNonNull(source, name).forEach((key, value) ->
                copy.put(requireText(key, name + " key"), requireText(value, name + " value")));
        return Collections.unmodifiableMap(copy);
    }

    private static String requireText(String value, String name) {
        if (Objects.requireNonNull(value, name).isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
