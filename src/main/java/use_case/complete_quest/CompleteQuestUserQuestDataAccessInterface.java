package use_case.complete_quest;

import entity.UserQuest;

import java.time.LocalDate;

/**
 * Storage for users' quest progress (Daniel's data).
 *
 * Complete Quest touches data owned by three people, so it asks for three
 * small interfaces instead of one big one (Interface Segregation Principle).
 * Each team member's DAO only implements the piece it owns.
 */
public interface CompleteQuestUserQuestDataAccessInterface {

    /** Returns null if the user never accepted this quest. */
    UserQuest getUserQuest(String username, String questId);

    void saveUserQuest(UserQuest userQuest);

    /** How many quests this user completed on the given calendar day. */
    int countCompletedOn(String username, LocalDate day);
}
