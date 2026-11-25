package entity.state;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Immutable saved values and completed nonrepeatable action IDs for one object. */
public final class ObjectState {
    private final String id;
    private final Map<String, String> values;
    private final Set<String> completedActionIds;

    public ObjectState(String id, Map<String, String> values, Set<String> completedActionIds) {
        this.id = requireText(id, "id");
        LinkedHashMap<String, String> valuesCopy = new LinkedHashMap<>();
        Objects.requireNonNull(values, "values").forEach((key, value) ->
                valuesCopy.put(requireText(key, "state key"), requireText(value, "state value")));
        this.values = Collections.unmodifiableMap(valuesCopy);
        LinkedHashSet<String> completedCopy = new LinkedHashSet<>();
        Objects.requireNonNull(completedActionIds, "completedActionIds").forEach(value ->
                completedCopy.add(requireText(value, "action ID")));
        this.completedActionIds = Collections.unmodifiableSet(completedCopy);
    }

    public String id() {
        return id;
    }

    public Map<String, String> values() {
        return values;
    }

    public Set<String> completedActionIds() {
        return completedActionIds;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ObjectState)) {
            return false;
        }
        ObjectState that = (ObjectState) other;
        return id.equals(that.id) && values.equals(that.values)
                && completedActionIds.equals(that.completedActionIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, values, completedActionIds);
    }

    private static String requireText(String value, String name) {
        if (Objects.requireNonNull(value, name).isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
