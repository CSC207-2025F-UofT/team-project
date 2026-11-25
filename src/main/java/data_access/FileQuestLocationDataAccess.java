package data_access;

import entity.SideQuest;
import java.util.ArrayList;
import java.util.List;
import use_case.find_nearby_quests.QuestLoadException;
import use_case.find_nearby_quests.QuestLocationDataAccessInterface;

/**
 * Lets the map (Use Case 4) read quests from the CSV quest storage without that
 * storage class knowing about the map (Adapter pattern).
 *
 * <p>It adapts {@link SideQuestDataAccessObject#getAllQuests()} to the one
 * operation the map needs. If the map's interface changes, only this class changes;
 * if the storage class changes, only this class has to follow.
 */
public class FileQuestLocationDataAccess implements QuestLocationDataAccessInterface {
    private final SideQuestDataAccessObject storage;

    /**
     * Creates the adapter.
     *
     * @param storage the CSV quest storage to read from
     */
    public FileQuestLocationDataAccess(SideQuestDataAccessObject storage) {
        this.storage = storage;
    }

    /**
     * Returns the stored quests that have a location. Quests without one are left
     * out, because they cannot be drawn on the map.
     *
     * @return the quests with a location
     * @throws QuestLoadException if the storage cannot be read
     */
    @Override
    public List<SideQuest> getQuestsWithLocation() throws QuestLoadException {
        List<SideQuest> allQuests;
        try {
            allQuests = storage.getAllQuests();
        } catch (RuntimeException e) {
            // Extension 2a: the map shows "Couldn't load side quests" with Retry.
            throw new QuestLoadException("Could not read the saved side quests.", e);
        }

        List<SideQuest> result = new ArrayList<>();
        if (allQuests == null) {
            return result;
        }
        for (SideQuest quest : allQuests) {
            if (quest != null && quest.getLocation() != null) {
                result.add(quest);
            }
        }
        return result;
    }
}
