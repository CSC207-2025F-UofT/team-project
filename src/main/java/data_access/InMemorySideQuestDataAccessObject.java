package data_access;

import entity.Location;
import entity.SideQuest;
import java.util.ArrayList;
import java.util.List;
import use_case.find_nearby_quests.QuestLocationDataAccessInterface;

/**
 * Keeps side quests in memory. Used by tests and to run the map before the
 * file-based quest storage is connected.
 */
public class InMemorySideQuestDataAccessObject implements QuestLocationDataAccessInterface {
    private static final String OUTDOOR = "false";
    private static final String INDOOR = "true";

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
                sample("Read in Queen's Park", OUTDOOR, 20, 43.6640, -79.3920,
                        List.of("Outdoors", "Reading")),
                sample("Sketch an exhibit at the ROM", INDOOR, 40, 43.6677, -79.3948,
                        List.of("Art")),
                sample("Kensington Market food crawl", OUTDOOR, 50, 43.6547, -79.4005,
                        List.of("Food")),
                sample("Sunset at the CN Tower", INDOOR, 60, 43.6426, -79.3871,
                        List.of("Sightseeing")),
                sample("Explore the Distillery District", OUTDOOR, 50, 43.6503, -79.3596,
                        List.of("History", "Food")),
                sample("Walk the Toronto Islands boardwalk", OUTDOOR, 80, 43.6163, -79.3740,
                        List.of("Outdoors")),
                sample("Hike the Scarborough Bluffs", OUTDOOR, 100, 43.7056, -79.2316,
                        List.of("Outdoors", "Fitness"))));
    }

    private static SideQuest sample(String name, String isIndoor, int exp,
                                    double latitude, double longitude, List<String> interests) {
        return new SideQuest(name, name, isIndoor, 0, 1, exp,
                new Location(latitude, longitude), interests);
    }

    @Override
    public List<SideQuest> getQuestsWithLocation() {
        List<SideQuest> result = new ArrayList<>();
        for (SideQuest quest : quests) {
            if (quest.getCoordinates() != null) {
                result.add(quest);
            }
        }
        return result;
    }
}
