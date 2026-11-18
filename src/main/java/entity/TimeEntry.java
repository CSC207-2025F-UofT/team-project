package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * One punch in / punch out interval for an activity.
 * An entry with no end time is "running" (the user has punched in but not out).
 */
public class TimeEntry {
    private final String id;
    private LocalDateTime start;
    private LocalDateTime end; // null while the entry is still running

    /** Creates a new running entry (punch in). */
    public TimeEntry(LocalDateTime start) {
        this(UUID.randomUUID().toString(), start, null);
    }

    /** Rebuilds an entry with a known id (used when loading from storage). */
    public TimeEntry(String id, LocalDateTime start, LocalDateTime end) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.start = Objects.requireNonNull(start, "start cannot be null");
        validate(start, end);
        this.end = end;
    }

    // --- getters ---
    public String getId() { return id; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public boolean isRunning() { return end == null; }

    /** Punch out. */
    public void stop(LocalDateTime when) {
        if (!isRunning()) {
            throw new IllegalStateException("Entry has already been stopped");
        }
        Objects.requireNonNull(when, "end time cannot be null");
        validate(start, when);
        this.end = when;
    }

    /** Edit a past entry. Both times are validated together so it never ends up invalid. */
    public void setTimes(LocalDateTime newStart, LocalDateTime newEnd) {
        Objects.requireNonNull(newStart, "start cannot be null");
        validate(newStart, newEnd);
        this.start = newStart;
        this.end = newEnd;
    }

    /** Length of the entry; for a running entry, the time elapsed so far. */
    public Duration getDuration() {
        return Duration.between(start, end != null ? end : LocalDateTime.now());
    }

    private static void validate(LocalDateTime start, LocalDateTime end) {
        if (end != null && end.isBefore(start)) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeEntry)) return false;
        TimeEntry other = (TimeEntry) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}