package plan4life.entities;

import java.time.LocalDateTime;
import java.util.*;

public class Schedule {
    private final int scheduleId;
    private final String type;    // "day", "week", etc.
    private final Map<String, String> activities = new LinkedHashMap<>();    // e.g., "Monday 9 AM" -> "Workout"
    private final List<ScheduledBlock> unlockedBlocks;
    private final List<ScheduledBlock> lockedBlocks;
    private final List<BlockedTime> blockedTimes;

    public Schedule(int scheduleId, String type) {
        this.scheduleId = scheduleId;
        this.type = type;

        this.unlockedBlocks = new ArrayList<>();
        this.lockedBlocks = new ArrayList<>();
        this.blockedTimes = new ArrayList<>();
    }

    public Schedule() {
        this(0, "testing");
    }

    public int getScheduleId() { return scheduleId; }
    public String getType() { return type; }

    public List<BlockedTime> getBlockedTimes() {
        return Collections.unmodifiableList(blockedTimes);
    }

    public List<ScheduledBlock> getLockedBlocks() {
        return Collections.unmodifiableList(lockedBlocks);
    }

    public List<ScheduledBlock> getUnlockedBlocks() {
        return Collections.unmodifiableList(unlockedBlocks);
    }

    public void addActivity(String timeSlot, String activity) {
        activities.put(timeSlot, activity);
    }

    public Map<String, String> getActivities() {
        return Collections.unmodifiableMap(activities);
    }

    public void populateRandomly() {
        activities.clear();
        String[] sampleActivities = {"Work", "Gym", "Lunch", "Relax", "Sleep"};
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri"};
        for (String day : days) {
            for (int i = 9; i <= 17; i += 2) {
                String time = day + " " + i + ":00";
                activities.put(time, sampleActivities[new Random().nextInt(sampleActivities.length)]);
            }
        }
    }

    public boolean overlapsWithExistingBlocks(LocalDateTime start, LocalDateTime end) {
        for (BlockedTime block : blockedTimes) {
            if (block.overlaps(start, end)) {
                return true;
            }
        }
        return false;
    }

    public boolean overlapsWithActivities(LocalDateTime start, LocalDateTime end) {
        for (ScheduledBlock block : lockedBlocks) {
            if (block.overlaps(start, end)) {
                return true;
            }
        }

        for (ScheduledBlock block : unlockedBlocks) {
            if (block.overlaps(start, end)) {
                return true;
            }
        }

        return false;
    }

    public void removeOverlappingActivities(LocalDateTime start, LocalDateTime end) {
        // This means remove block if block overlaps this range
        unlockedBlocks.removeIf(block -> block.overlaps(start, end));
        lockedBlocks.removeIf(block -> block.overlaps(start, end));
    }

    public void addBlockedTime(BlockedTime block) {
        blockedTimes.add(block);
    }

    public void removeBlockedTime(BlockedTime block) {
        blockedTimes.remove(block);
    }
}

