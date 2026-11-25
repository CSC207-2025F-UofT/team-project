package use_case.view_dashboard;

import entity.UserQuest;

import java.util.List;

/** Storage for users' quest progress (Daniel's data). */
public interface ViewDashboardUserQuestDataAccessInterface {

    /** All of this user's quests, in any order. Empty list if none. */
    List<UserQuest> getUserQuests(String username);
}
