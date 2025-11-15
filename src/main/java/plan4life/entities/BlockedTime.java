package plan4life.entities;

import java.time.LocalDateTime;

public class BlockedTime {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final String description;
    private final int columnIndex;

    public BlockedTime(LocalDateTime start, LocalDateTime end, String description, int columnIndex) {
        this.start = start;
        this.end = end;
        this.description = description;
        this.columnIndex = columnIndex;
    }

    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public String getDescription() { return description; }
    public int getColumnIndex() { return columnIndex; }

    // Domain rule: check if two intervals overlap
    public boolean overlaps(LocalDateTime otherStart, LocalDateTime otherEnd) {
        return !(otherEnd.isBefore(start) || otherStart.isAfter(end));
    }
}