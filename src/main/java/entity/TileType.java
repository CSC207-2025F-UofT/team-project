package entity;

/**
 * Describes a terrain type's numeric identity and walking rule.
 * Implementations must be immutable so they can be shared safely across a map.
 */
public interface TileType {
    /**
     * Returns this terrain type's stable numeric ID.
     * IDs must be unique among types registered together; callers should compare IDs
     * when identifying terrain types instead of relying on object identity.
     *
     * @return the same terrain ID on every call
     */
    int getId();

    /**
     * Reports whether this terrain permits walking, without changing any state.
     *
     * @return true if walking is allowed, false otherwise; the result must stay constant
     */
    boolean isWalkable();
}
