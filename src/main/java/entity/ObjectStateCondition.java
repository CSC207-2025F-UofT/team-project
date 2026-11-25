package entity;

import java.util.Objects;

/** Requires an existing object's state value to match a configured string exactly. */
public final class ObjectStateCondition implements Condition {
    private final String objectId;
    private final String key;
    private final String expectedValue;

    public ObjectStateCondition(String objectId, String key, String expectedValue) {
        this.objectId = requireText(objectId, "objectId");
        this.key = requireText(key, "key");
        this.expectedValue = requireText(expectedValue, "expectedValue");
    }

    @Override
    public boolean test(RoomReadState facts) {
        return expectedValue.equals(Objects.requireNonNull(facts, "facts").objectState(objectId, key));
    }

    private static String requireText(String value, String name) {
        if (Objects.requireNonNull(value, name).isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
