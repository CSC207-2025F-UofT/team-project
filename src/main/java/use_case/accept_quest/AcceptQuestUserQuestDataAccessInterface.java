package use_case.accept_quest;

import entity.UserQuest;

/** Storage for users' quest progress (Daniel's data). */
public interface AcceptQuestUserQuestDataAccessInterface {

    /** Returns null if the user has never accepted this quest. */
    UserQuest getUserQuest(String username, String questId);

    void saveUserQuest(UserQuest userQuest);
}
