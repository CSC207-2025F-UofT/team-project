package plan4life.entities;

import java.time.LocalDateTime;

public class ScheduledBlock {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final String activityName;
    private final boolean locked;

    public ScheduledBlock(LocalDateTime start, LocalDateTime end, String activityName, boolean locked) {
        this.start = start;
        this.end = end;
        this.activityName = activityName;
        this.locked = locked;
    }

    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public String getActivityName() { return activityName; }
    public boolean isLocked() { return locked; }

    public boolean overlaps(LocalDateTime otherStart, LocalDateTime otherEnd) {
        return !(otherEnd.isBefore(start) || otherStart.isAfter(end));
    }
}