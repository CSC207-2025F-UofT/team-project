package use_case.accept_quest;

import entity.SideQuest;

/** Read-only side quest lookup (Isaac's data). */
public interface AcceptQuestSideQuestDataAccessInterface {

    /** Returns null if no such quest. */
    SideQuest getSideQuest(String questId);
}
