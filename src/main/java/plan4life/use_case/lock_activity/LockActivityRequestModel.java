package plan4life.use_case.lock_activity;

import java.util.Set;

public class LockActivityRequestModel {
    private final int scheduleId;
    private final Set<String> lockedSlots;

    public LockActivityRequestModel(int scheduleId, Set<String> lockedSlots) {
        this.scheduleId = scheduleId;
        this.lockedSlots = lockedSlots;
    }

    public int getScheduleId() { return scheduleId; }
    public Set<String> getLockedSlots() {
        return lockedSlots;
    }
}


