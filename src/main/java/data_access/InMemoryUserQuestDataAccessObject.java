package data_access;

import entity.QuestStatus;
import entity.UserQuest;
import use_case.accept_quest.AcceptQuestUserQuestDataAccessInterface;
import use_case.cancel_quest.CancelQuestUserQuestDataAccessInterface;
import use_case.complete_quest.CompleteQuestUserQuestDataAccessInterface;
import use_case.view_dashboard.ViewDashboardUserQuestDataAccessInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps users' quest progress in memory. Used by the tests, and by the app
 * until the JSON file version exists. Data is lost when the app closes.
 *
 * One class can implement many small interfaces: each use case only sees the
 * methods it asked for, but all of them share the same data here.
 */
public class InMemoryUserQuestDataAccessObject implements
        AcceptQuestUserQuestDataAccessInterface,
        CompleteQuestUserQuestDataAccessInterface,
        CancelQuestUserQuestDataAccessInterface,
        ViewDashboardUserQuestDataAccessInterface {

    // Java note: Map<K, V> is a dict. LinkedHashMap keeps insertion order.
    private final Map<String, UserQuest> userQuests = new LinkedHashMap<>();

    private static String key(String username, String questId) {
        return username + "::" + questId;
    }

    @Override
    public UserQuest getUserQuest(String username, String questId) {
        return userQuests.get(key(username, questId));
    }

    @Override
    public void saveUserQuest(UserQuest userQuest) {
        userQuests.put(key(userQuest.getUsername(), userQuest.getQuestId()), userQuest);
    }

    @Override
    public int countCompletedOn(String username, LocalDate day) {
        int count = 0;
        for (UserQuest uq : userQuests.values()) {
            if (uq.getUsername().equals(username)
                    && uq.getStatus() == QuestStatus.COMPLETED
                    && uq.getCompletedAt().toLocalDate().equals(day)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public List<UserQuest> getUserQuests(String username) {
        List<UserQuest> result = new ArrayList<>();
        for (UserQuest uq : userQuests.values()) {
            if (uq.getUsername().equals(username)) {
                result.add(uq);
            }
        }
        return result;
    }
}
