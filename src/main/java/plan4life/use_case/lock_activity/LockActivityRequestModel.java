package plan4life.use_case.lock_activity;

import java.util.Set;

public class LockActivityRequestModel {
    private final Set<String> lockedSlots;

    public LockActivityRequestModel(Set<String> lockedSlots) {
        this.lockedSlots = lockedSlots;
    }

    public Set<String> getLockedSlots() {
        return lockedSlots;
    }
}

