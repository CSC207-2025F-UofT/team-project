package use_case.view_dashboard;

import entity.SideQuest;

/** Read-only side quest lookup (Isaac's data). */
public interface ViewDashboardSideQuestDataAccessInterface {

    /** Returns null if no such quest. */
    SideQuest getSideQuest(String questId);
}
