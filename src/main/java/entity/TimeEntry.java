package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/*
 * TimeEntry
 * ---------
 * Represents ONE punch in / punch out interval for an activity
 * (e.g. "worked on CSC207 from 2:00 PM to 3:30 PM").
 *
 * An entry with no end time is "running": the user has punched in
 * but has not punched out yet.
 *
 * Main functions:
 *  - TimeEntry(start)             : create a new running entry (punch in)
 *  - TimeEntry(id, start, end)    : rebuild an existing entry (e.g. loaded from storage)
 *  - stop(when)                   : punch out a running entry
 *  - setTimes(newStart, newEnd)   : edit a past entry's start and end together
 *  - getDuration()                : length of the entry (elapsed time if still running)
 *  - isRunning()                  : true if the entry has no end time yet
 *  - getId() / getStart() / getEnd() : read-only access to the fields
 *
 * This class only depends on java.time and java.util, so it has no
 * knowledge of the GUI, database, or any outer layer.
 */
public class TimeEntry {
    // Unique id so a single entry can be found, edited, or deleted later.
    private final String id;

    // When the user punched in.
    private LocalDateTime start;

    // When the user punched out; null means the entry is still running.
    private LocalDateTime end;

    /**
     * Creates a new running entry (punch in) with a generated id.
     */
    public TimeEntry(LocalDateTime start) {
        this(UUID.randomUUID().toString(), start, null);
    }

    /**
     * Rebuilds an entry with a known id and end time.
     * Used when loading saved entries from storage.
     */
    public TimeEntry(String id, LocalDateTime start, LocalDateTime end) {
        // Id and start are always required.
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.start = Objects.requireNonNull(start, "start cannot be null");
        // End is optional, but if present it must not come before start.
        validate(start, end);
        this.end = end;
    }

    // ---------- getters ----------

    /** Returns this entry's unique id. */
    public String getId() {
        return id;
    }

    /** Returns the punch-in time. */
    public LocalDateTime getStart() {
        return start;
    }

    /** Returns the punch-out time, or null if the entry is still running. */
    public LocalDateTime getEnd() {
        return end;
    }

    /** Returns true if the user has punched in but not out yet. */
    public boolean isRunning() {
        return end == null;
    }

    // ---------- behaviour ----------

    /**
     * Punches out a running entry at the given time.
     * The time is passed in (not read from the clock) so tests can use fixed times.
     */
    public void stop(LocalDateTime when) {
        // Can't stop an entry that already has an end time.
        if (!isRunning()) {
            throw new IllegalStateException("Entry has already been stopped");
        }
        Objects.requireNonNull(when, "end time cannot be null");
        validate(start, when);
        this.end = when;
    }

    /**
     * Edits a past entry. Start and end are set together and validated together,
     * so the entry can never end up with an end before its start.
     */
    public void setTimes(LocalDateTime newStart, LocalDateTime newEnd) {
        Objects.requireNonNull(newStart, "start cannot be null");
        validate(newStart, newEnd);
        this.start = newStart;
        this.end = newEnd;
    }

    /**
     * Returns the length of this entry.
     * For a running entry, this is the time elapsed so far.
     */
    public Duration getDuration() {
        LocalDateTime endPoint = (end != null) ? end : LocalDateTime.now();
        return Duration.between(start, endPoint);
    }

    // ---------- helpers ----------

    /** Throws if an end time is given and comes before the start time. */
    private static void validate(LocalDateTime start, LocalDateTime end) {
        if (end != null && end.isBefore(start)) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }
    }

    // ---------- equality (by id only) ----------

    /** Two entries are equal if they share an id, even if their times were edited. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeEntry)) {
            return false;
        }
        TimeEntry other = (TimeEntry) o;
        return id.equals(other.id);
    }

    /** Hash code based on the id, consistent with equals. */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}