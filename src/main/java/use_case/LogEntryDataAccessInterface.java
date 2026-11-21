package use_case;

import entities.LogEntry;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Storage for completed workout log entries.
 * A log entry's time is its unique key. Entries are immutable, so they are
 * only ever added or deleted, never updated.
 */
public interface LogEntryDataAccessInterface {

    /** Saves a finished workout. */
    void save(LogEntry entry);

    /** @return all log entries, oldest first. */
    List<LogEntry> getAll();

    /** @return entries whose time falls within [from, to], inclusive, oldest first. */
    List<LogEntry> getBetween(LocalDateTime from, LocalDateTime to);

    /** @return entries that contain sets for the given exercise, oldest first. */
    List<LogEntry> getByExerciseName(String exerciseName);

    /**
     * Deletes the entry with the given time.
     * @return true if an entry was deleted, false if none matched.
     */
    boolean delete(LocalDateTime time);
}
