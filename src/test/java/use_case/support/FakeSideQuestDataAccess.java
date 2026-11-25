package use_case.support;

import entity.Location;
import entity.SideQuest;
import use_case.accept_quest.AcceptQuestSideQuestDataAccessInterface;
import use_case.complete_quest.CompleteQuestSideQuestDataAccessInterface;
import use_case.view_dashboard.ViewDashboardSideQuestDataAccessInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test stand-in for Isaac's side quest storage. Lives in the test folder
 * only, so it never competes with his real DAO.
 */
public class FakeSideQuestDataAccess implements
        AcceptQuestSideQuestDataAccessInterface,
        CompleteQuestSideQuestDataAccessInterface,
        ViewDashboardSideQuestDataAccessInterface {

    private final Map<String, SideQuest> quests = new HashMap<>();

    @Override
    public SideQuest getSideQuest(String questId) {
        return quests.get(questId);
    }

    public void add(String questId, SideQuest quest) {
        quests.put(questId, quest);
    }

    /** A valid side quest with the given name and EXP. */
    public static SideQuest quest(String name, int exp) {
        return new SideQuest(name, name, "indoor", "free", "under_30_minutes", exp,
                new Location(43.66, -79.39), List.of("food"));
    }
}
