package entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Resolves numeric IDs to an explicitly supplied, fixed set of terrain types. */
public final class TileTypeRegistry {
    private final Map<Integer, TileType> typesById = new HashMap<>();

    /**
     * Registers terrain objects without retaining the caller's list.
     * Each object must obey TileType's immutability contract.
     *
     * @param types terrain types to register; an empty list is allowed
     * @throws IllegalArgumentException if the list or an entry is null, or IDs are duplicated
     */
    public TileTypeRegistry(List<TileType> types) {
        if (types == null) {
            throw new IllegalArgumentException("Tile types must not be null.");
        }
        for (TileType type : types) {
            if (type == null) {
                throw new IllegalArgumentException("A registered tile type must not be null.");
            }
            int id = type.getId();
            if (typesById.containsKey(id)) {
                throw new IllegalArgumentException("Duplicate tile id: " + id);
            }
            typesById.put(id, type);
        }
    }

    /**
     * Finds a registered terrain type by its numeric ID.
     *
     * @param id numeric terrain ID
     * @return the registered object, shared by all lookups of this ID
     * @throws IllegalArgumentException if the ID is not registered
     */
    public TileType fromId(int id) {
        TileType type = typesById.get(id);
        if (type == null) {
            throw new IllegalArgumentException("Unknown tile id: " + id);
        }
        return type;
    }
}
