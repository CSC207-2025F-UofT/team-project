package plan4life.entities;

import java.time.LocalDateTime;

public class BlockedTime {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final String description;

    public BlockedTime(LocalDateTime start, LocalDateTime end, String description) {
        this.start = start;
        this.end = end;
        this.description = description;
    }

    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public String getDescription() { return description; }

    // Domain rule: check if two intervals overlap
    public boolean overlaps(LocalDateTime otherStart, LocalDateTime otherEnd) {
        return !(otherEnd.isBefore(start) || otherStart.isAfter(end));
    }
}