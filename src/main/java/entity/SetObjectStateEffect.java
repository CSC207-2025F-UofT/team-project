package entity;

import java.util.Objects;

/** Deterministic assignment to an existing object's configured state key and value. */
public final class SetObjectStateEffect implements Effect {
    private final String objectId;
    private final String key;
    private final String value;

    public SetObjectStateEffect(String objectId, String key, String value) {
        this.objectId = requireText(objectId, "objectId");
        this.key = requireText(key, "key");
        this.value = requireText(value, "value");
    }

    public String objectId() {
        return objectId;
    }

    public String key() {
        return key;
    }

    public String value() {
        return value;
    }

    @Override
    public void validate(RoomReadState facts) {
        if (!Objects.requireNonNull(facts, "facts").acceptsObjectState(objectId, key, value)) {
            throw new DomainException(ResultCode.INVALID_EFFECT,
                    "Invalid state effect for object " + objectId + ": " + key + " = " + value);
        }
    }

    @Override
    public void apply(RoomMutation target) {
        Objects.requireNonNull(target, "target").setObjectState(objectId, key, value);
    }

    private static String requireText(String value, String name) {
        if (Objects.requireNonNull(value, name).isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
