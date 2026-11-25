package data_access;

import entity.Location;
import entity.SideQuest;
import java.util.ArrayList;
import java.util.List;
import use_case.find_nearby_quests.QuestLocationDataAccessInterface;

/**
 * PLACEHOLDER quest source for Use Case 4. Keeps side quests in memory.
 *
 * <p>It exists so the map can be run and tested before the file-based quest
 * storage (owned by Use Case 6) can load quests. Once that class implements
 * {@link QuestLocationDataAccessInterface}, {@code AppBuilder} switches to it and
 * this class is kept only for tests. The interactor does not change, because it
 * depends on the interface, not on this class.
 */
public class InMemorySideQuestDataAccessObject implements QuestLocationDataAccessInterface {
    // TODO for Serena: Sorry for meddling with your OUTDOOR and INDOOR constants, but the intention of the isIndoor string could also be "BOTH"
    private static final String OUTDOOR = "false";
    private static final String INDOOR = "true";
    private static final String SAMPLE_BUDGET = "free";
    private static final String SAMPLE_EXPECTED_TIME = "under_60_minutes";

    private final List<SideQuest> quests;

    /**
     * Creates the data access object with the given quests.
     *
     * @param quests the quests to keep
     */
    public InMemorySideQuestDataAccessObject(List<SideQuest> quests) {
        this.quests = new ArrayList<>(quests);
    }

    /**
     * Creates a data access object holding sample quests around downtown Toronto,
     * at a range of distances from U of T St. George.
     *
     * @return the data access object
     */
    public static InMemorySideQuestDataAccessObject withSampleQuests() {
        return new InMemorySideQuestDataAccessObject(List.of(
                sample("sample-1", "Read in Queen's Park", OUTDOOR, 20, 43.6640, -79.3920,
                        List.of("Outdoors", "Reading")),
                sample("sample-2", "Sketch an exhibit at the ROM", INDOOR, 40, 43.6677, -79.3948,
                        List.of("Art")),
                sample("sample-3", "Kensington Market food crawl", OUTDOOR, 50, 43.6547, -79.4005,
                        List.of("Food")),
                sample("sample-4", "Sunset at the CN Tower", INDOOR, 60, 43.6426, -79.3871,
                        List.of("Sightseeing")),
                sample("sample-5", "Explore the Distillery District", OUTDOOR, 50,
                        43.6503, -79.3596,
                        List.of("History", "Food")),
                sample("sample-6", "Walk the Toronto Islands boardwalk", OUTDOOR, 80,
                        43.6163, -79.3740,
                        List.of("Outdoors")),
                sample("sample-7", "Hike the Scarborough Bluffs", OUTDOOR, 100, 43.7056, -79.2316,
                        List.of("Outdoors", "Fitness"))));
    }

    private static SideQuest sample(String id, String name, String isIndoor, int exp,
                                    double latitude, double longitude, List<String> interests) {
        return new SideQuest(name, id, isIndoor, SAMPLE_BUDGET, SAMPLE_EXPECTED_TIME, exp, 0,
                new Location(latitude, longitude), interests, false);
    }

    @Override
    public List<SideQuest> getQuestsWithLocation() {
        List<SideQuest> result = new ArrayList<>();
        for (SideQuest quest : quests) {
            if (quest.getLocation() != null) {
                result.add(quest);
            }
        }
        return result;
    }
}
