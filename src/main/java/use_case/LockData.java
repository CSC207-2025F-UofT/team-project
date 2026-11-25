package use_case;

import entity.LockKind;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** Door requirements and progress; contains no answer or mutable domain state. */
public final class LockData {
    private final String lockId;
    private final String ownerId;
    private final String label;
    private final LockKind kind;
    private final boolean unlocked;
    private final List<ItemData> requiredItems;
    private final Set<String> heldRequiredIds;
    private final boolean canUnlock;

    public LockData(String lockId, String ownerId, String label, LockKind kind,
                    boolean unlocked, List<ItemData> requiredItems,
                    Set<String> heldRequiredIds, boolean canUnlock) {
        this.lockId = Objects.requireNonNull(lockId, "lockId");
        this.ownerId = Objects.requireNonNull(ownerId, "ownerId");
        this.label = Objects.requireNonNull(label, "label");
        this.kind = Objects.requireNonNull(kind, "kind");
        this.unlocked = unlocked;
        this.requiredItems = List.copyOf(requiredItems);
        LinkedHashSet<String> held = new LinkedHashSet<>();
        for (String id : heldRequiredIds) held.add(Objects.requireNonNull(id, "held item ID"));
        this.heldRequiredIds = Collections.unmodifiableSet(held);
        this.canUnlock = canUnlock;
    }

    public String lockId() { return lockId; }
    public String ownerId() { return ownerId; }
    public String label() { return label; }
    public LockKind kind() { return kind; }
    public boolean unlocked() { return unlocked; }
    public List<ItemData> requiredItems() { return requiredItems; }
    public Set<String> heldRequiredIds() { return heldRequiredIds; }
    public boolean canUnlock() { return canUnlock; }
}
