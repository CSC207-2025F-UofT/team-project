package entity;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * One user's progress on one side quest. Created when the user accepts a quest.
 *
 * Progress lives here instead of on SideQuest because SideQuest is the shared
 * catalogue: if status were stored on SideQuest, one user completing a quest
 * would mark it completed for every user.
 *
 * Business rules (enforced by this class, whoever calls it):
 * 1. A quest starts IN_PROGRESS.
 * 2. IN_PROGRESS can only move to COMPLETED or CANCELLED.
 * 3. COMPLETED and CANCELLED are final.
 * 4. A quest can't be completed until MIN_MINUTES_BEFORE_COMPLETE have passed.
 */
public class UserQuest {

    /** Minimum minutes between accepting and completing. Stops XP farming. */
    public static final int MIN_MINUTES_BEFORE_COMPLETE = 10;

    // Java note: "final" fields can only be set once, in the constructor.
    private final String username;
    private final String questId;
    private final LocalDateTime startedAt;

    private QuestStatus status;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;

    /** Creates a newly accepted quest. Used by Accept Quest. */
    public UserQuest(String username, String questId, LocalDateTime startedAt) {
        this(username, questId, QuestStatus.IN_PROGRESS, startedAt, null, null);
    }

    /**
     * Rebuilds a UserQuest in any state. Used when loading saved data.
     *
     * Java note: two constructors with different parameters is called
     * "overloading". Python would use default arguments instead.
     *
     * @throws IllegalArgumentException if a required value is missing
     */
    public UserQuest(String username, String questId, QuestStatus status,
                     LocalDateTime startedAt, LocalDateTime completedAt,
                     LocalDateTime cancelledAt) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if (questId == null || questId.isBlank()) {
            throw new IllegalArgumentException("Quest id cannot be blank");
        }
        if (status == null || startedAt == null) {
            throw new IllegalArgumentException("Status and start time are required");
        }
        this.username = username;
        this.questId = questId;
        this.status = status;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.cancelledAt = cancelledAt;
    }

    public String getUsername() {
        return username;
    }

    public String getQuestId() {
        return questId;
    }

    public QuestStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    /** Null unless the quest is completed. */
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    /** Null unless the quest is cancelled. */
    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public boolean isInProgress() {
        return status == QuestStatus.IN_PROGRESS;
    }

    /** The time this quest last changed status. Used to sort the dashboard. */
    public LocalDateTime getLastUpdated() {
        if (status == QuestStatus.COMPLETED) {
            return completedAt;
        }
        if (status == QuestStatus.CANCELLED) {
            return cancelledAt;
        }
        return startedAt;
    }

    /** Minutes the user still has to wait before completing. 0 means ready. */
    public long minutesUntilCompletable(LocalDateTime now) {
        long elapsed = Duration.between(startedAt, now).toMinutes();
        return Math.max(0, MIN_MINUTES_BEFORE_COMPLETE - elapsed);
    }

    public boolean meetsMinimumTime(LocalDateTime now) {
        return minutesUntilCompletable(now) == 0;
    }

    /**
     * Marks the quest completed.
     *
     * @throws IllegalStateException if the quest is not in progress
     */
    public void complete(LocalDateTime now) {
        requireInProgress();
        status = QuestStatus.COMPLETED;
        completedAt = now;
    }

    /**
     * Marks the quest cancelled.
     *
     * @throws IllegalStateException if the quest is not in progress
     */
    public void cancel(LocalDateTime now) {
        requireInProgress();
        status = QuestStatus.CANCELLED;
        cancelledAt = now;
    }

    private void requireInProgress() {
        if (!isInProgress()) {
            throw new IllegalStateException("Quest is " + status + ", not IN_PROGRESS");
        }
    }
}
