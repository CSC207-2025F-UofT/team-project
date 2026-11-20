package use_case.dao_interfaces;

import entity.Activity;
import java.util.List;
import java.util.Optional;

/*
 * ActivityDataAccessInterface
 * ---------------------------
 * The storage operations that use cases need for activities. It is shared by
 * most activity-related use cases (create, edit, delete, and later the time
 * use cases, which load and save an activity to change its time entries).
 *
 * Use cases depend only on this interface. The real implementation (file or
 * database) is written later in the data access layer, so nothing here knows
 * how activities are actually stored.
 *
 * A saved Activity includes its TimeEntry list, so saving or deleting an
 * activity also saves or deletes its sessions.
 *
 * Every method takes the username because each user has their own activities.
 *
 * Main functions:
 *  - getActivity(username, activityId) : load one activity
 *  - getActivities(username)           : load all of a user's activities
 *  - existsByName(username, name)      : check for a duplicate name
 *  - save(username, activity)          : create or update an activity
 *  - delete(username, activityId)      : remove an activity and its entries
 */
public interface ActivityDataAccessInterface {

    /**
     * Returns the user's activity with this id, or empty if it doesn't exist.
     * TODO (data access layer, later): implement with file/DB storage.
     */
    Optional<Activity> getActivity(String username, String activityId);

    /**
     * Returns all of the user's activities (empty list if none).
     * TODO (data access layer, later): implement with file/DB storage.
     */
    List<Activity> getActivities(String username);

    /**
     * Returns true if the user already has an activity with this name, ignoring case.
     * TODO (data access layer, later): implement with file/DB storage.
     */
    boolean existsByName(String username, String activityName);

    /**
     * Saves the activity: inserts it if its id is new, otherwise overwrites the
     * stored version (including its time entries).
     * TODO (data access layer, later): implement with file/DB storage.
     */
    void save(String username, Activity activity);

    /**
     * Permanently removes the activity and all of its time entries.
     * TODO (data access layer, later): implement with file/DB storage.
     */
    void delete(String username, String activityId);
}