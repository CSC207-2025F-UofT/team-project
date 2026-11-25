package use_case.find_nearby_quests;

/**
 * Input boundary for Use Case 4: Find a Nearby Side Quest on the Map.
 *
 * <p>Returns nothing: results go out through the {@link FindNearbyQuestsOutputBoundary}.
 */
public interface FindNearbyQuestsInputBoundary {
    /**
     * Finds the side quests within the requested radius of the user's location.
     *
     * @param inputData the requested search radius
     */
    void execute(FindNearbyQuestsInputData inputData);
}
