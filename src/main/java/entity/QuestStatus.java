package entity;

/**
 * Where one user is on one side quest.
 * "Not Started" has no value here on purpose: a quest is not started for a
 * user when no UserQuest exists for them yet.
 */
public enum QuestStatus {
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
