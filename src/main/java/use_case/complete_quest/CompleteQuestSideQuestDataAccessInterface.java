package use_case.complete_quest;

import entity.SideQuest;

/**
 * Read-only side quest lookup (Isaac's data). Complete Quest never changes a
 * quest, so there's no save method.
 */
public interface CompleteQuestSideQuestDataAccessInterface {

    /** Returns null if no such quest. */
    SideQuest getSideQuest(String questId);
}
