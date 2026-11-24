package use_case.cancel_quest;

import entity.UserQuest;

/**
 * Storage for users' quest progress (Daniel's data). Cancelling doesn't need
 * the quest itself, so this is the only interface this use case needs.
 */
public interface CancelQuestUserQuestDataAccessInterface {

    /** Returns null if the user never accepted this quest. */
    UserQuest getUserQuest(String username, String questId);

    void saveUserQuest(UserQuest userQuest);
}
