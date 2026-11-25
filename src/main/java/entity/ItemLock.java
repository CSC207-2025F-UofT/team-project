package entity;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/** Door gate that requires possession of all configured IDs and retains all items. */
public final class ItemLock extends Lock {
    private final Set<String> requiredItemIds;

    public ItemLock(String id, String description, String successText, Set<String> requiredItemIds) {
        super(id, description, successText, LockKind.ITEM);
        if (requiredItemIds == null || requiredItemIds.isEmpty()
                || requiredItemIds.stream().anyMatch(value -> value == null || value.isBlank())) {
            throw new IllegalArgumentException("An item lock requires nonempty item IDs");
        }
        this.requiredItemIds = Collections.unmodifiableSet(new LinkedHashSet<>(requiredItemIds));
    }

    public Set<String> requiredItemIds() { return requiredItemIds; }

    DomainResult tryUnlock(Inventory inventory) {
        if (isUnlocked()) return new DomainResult(ResultCode.ALREADY_UNLOCKED, false, "The door is already unlocked.");
        if (!inventory.containsAll(requiredItemIds)) {
            return new DomainResult(ResultCode.MISSING_ITEM, false, "You do not hold every required item.");
        }
        markUnlocked();
        return new DomainResult(ResultCode.SUCCESS, true, successText());
    }
}
