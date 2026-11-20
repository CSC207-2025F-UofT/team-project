package entity;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/*
 * Activity
 * --------
 * Represents something the user tracks time against
 * (e.g. "CSC207 project", "Gaming").
 *
 * An activity has a name, an optional time budget ("time I'm willing to
 * spend"), and a collection of TimeEntry objects recording each session.
 * Entries are stored in a map keyed by entry id.
 *
 * Main functions:
 *  - Activity(name)                    : create a new activity with no budget
 *  - Activity(name, timeBudget)        : create a new activity with an optional budget
 *  - Activity(id, name, timeBudget)    : rebuild an existing activity (e.g. from storage)
 *  - setName / setTimeBudget           : edit the activity (validated)
 *  - addTimeEntry(entry)               : add a session (only one running entry allowed)
 *  - getTimeEntry(id) / removeTimeEntry(id) : look up or delete one session
 *  - getRunningEntry()                 : the currently running session, if any
 *  - getTotalTime()                    : total time across all entries
 *  - isOverBudget()                    : true if total time exceeds the budget
 *  - getters                           : id, name, budget, entries
 *
 * This class does not know about users, the GUI, or storage. Which user owns
 * an activity is handled by the use case / data access layers, not here.
 */
public class Activity {
    // Unique id so the activity stays identifiable even if its name is edited.
    private final String id;

    // Display name (trimmed, never blank).
    private String name;

    // Optional budget; null means no budget has been set.
    private Duration timeBudget;

    // All sessions for this activity, keyed by entry id, in insertion order.
    private final Map<String, TimeEntry> timeEntries = new LinkedHashMap<>();

    /** Creates a new activity with a generated id and no budget. */
    public Activity(String name) {
        this(UUID.randomUUID().toString(), name, null);
    }

    /** Creates a new activity with a generated id and an optional budget. */
    public Activity(String name, Duration timeBudget) {
        this(UUID.randomUUID().toString(), name, timeBudget);
    }

    /** Rebuilds an activity with a known id (used when loading from storage). */
    public Activity(String id, String name, Duration timeBudget) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        // Setters are used so the same validation applies everywhere.
        setName(name);
        setTimeBudget(timeBudget);
    }

    // ---------- getters ----------

    /** Returns this activity's unique id. */
    public String getId() {
        return id;
    }

    /** Returns the activity's name. */
    public String getName() {
        return name;
    }

    /** Returns the time budget, or empty if none is set. */
    public Optional<Duration> getTimeBudget() {
        return Optional.ofNullable(timeBudget);
    }

    /** Returns a read-only snapshot of all entries, in the order they were added. */
    public List<TimeEntry> getTimeEntries() {
        return Collections.unmodifiableList(new ArrayList<>(timeEntries.values()));
    }

    // ---------- setters (validated; used by Edit Activity) ----------

    /** Changes the name. Throws if null or blank. */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Activity name cannot be blank");
        }
        this.name = name.trim();
    }

    /** Sets the time budget. Pass null to remove it; otherwise it must be positive. */
    public void setTimeBudget(Duration timeBudget) {
        if (timeBudget != null && (timeBudget.isNegative() || timeBudget.isZero())) {
            throw new IllegalArgumentException("Time budget must be positive");
        }
        this.timeBudget = timeBudget;
    }

    // ---------- time entries ----------

    /**
     * Adds a session to this activity.
     * Throws if the entry is running and another entry is already running.
     */
    public void addTimeEntry(TimeEntry entry) {
        Objects.requireNonNull(entry, "entry cannot be null");
        // Only one punch-in can be active per activity at a time.
        if (entry.isRunning() && getRunningEntry().isPresent()) {
            throw new IllegalStateException("This activity already has a running entry");
        }
        timeEntries.put(entry.getId(), entry);
    }

    /** Finds one entry by id, or empty if it doesn't exist. */
    public Optional<TimeEntry> getTimeEntry(String entryId) {
        return Optional.ofNullable(timeEntries.get(entryId));
    }

    /** Removes an entry by id. Returns true if it existed and was removed. */
    public boolean removeTimeEntry(String entryId) {
        return timeEntries.remove(entryId) != null;
    }

    /** Returns the currently running entry, or empty if the user isn't punched in. */
    public Optional<TimeEntry> getRunningEntry() {
        for (TimeEntry entry : timeEntries.values()) {
            if (entry.isRunning()) {
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }

    // ---------- time calculations ----------

    /** Returns the total time across all entries (running entries count elapsed time). */
    public Duration getTotalTime() {
        Duration total = Duration.ZERO;
        for (TimeEntry entry : timeEntries.values()) {
            total = total.plus(entry.getDuration());
        }
        return total;
    }

    /** Returns true if a budget is set and total time has gone past it. */
    public boolean isOverBudget() {
        return timeBudget != null && getTotalTime().compareTo(timeBudget) > 0;
    }

    // ---------- equality (by id only) ----------

    /** Two activities are equal if they share an id, even if the name was edited. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activity)) {
            return false;
        }
        Activity other = (Activity) o;
        return id.equals(other.id);
    }

    /** Hash code based on the id, consistent with equals. */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}