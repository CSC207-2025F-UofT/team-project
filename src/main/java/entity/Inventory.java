package entity;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** The authoritative location of held items; possession is matched by exact ID. */
public final class Inventory {
    private final Map<String, Collectable> items = new LinkedHashMap<>();

    public boolean contains(String itemId) { return items.containsKey(itemId); }
    public boolean containsAll(Set<String> itemIds) { return items.keySet().containsAll(Objects.requireNonNull(itemIds)); }
    public Collectable require(String itemId) {
        Collectable item = items.get(itemId);
        if (item == null) throw new DomainException(ResultCode.MISSING_ITEM, "Item is not held: " + itemId);
        return item;
    }
    public List<Collectable> items() { return List.copyOf(items.values()); }
    public Set<String> itemIds() { return Collections.unmodifiableSet(new LinkedHashSet<>(items.keySet())); }

    void add(Collectable item) {
        Objects.requireNonNull(item, "item");
        if (items.containsKey(item.id())) {
            throw new DomainException(ResultCode.INVALID_ACQUISITION, "Duplicate inventory item: " + item.id());
        }
        items.put(item.id(), item);
    }
}
