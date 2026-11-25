package use_case.find_nearby_quests;

import entity.SideQuest;
import java.util.List;

/** The only quest data Use Case 4 needs: the quests that have a location. */
public interface QuestLocationDataAccessInterface {
    /**
     * Returns every side quest that has a location.
     *
     * @return the quests
     * @throws QuestLoadException if the quests cannot be loaded
     */
    List<SideQuest> getQuestsWithLocation() throws QuestLoadException;
}
