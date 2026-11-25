package use_case.find_nearby_quests;

/**
 * Output boundary for Use Case 4. There is one method per outcome of the use case.
 */
public interface FindNearbyQuestsOutputBoundary {
    /**
     * Steps 2-3 (and 3a when the list is empty): show the map with the nearby quests.
     *
     * @param outputData the centre, radius, and nearby quests
     */
    void prepareSuccessView(FindNearbyQuestsOutputData outputData);

    /**
     * Extension 2a: quests could not be loaded. Show the map and marker with no pins.
     *
     * @param centreLatitude latitude of the user's location
     * @param centreLongitude longitude of the user's location
     * @param errorMessage why loading failed
     */
    void prepareLoadFailView(double centreLatitude, double centreLongitude, String errorMessage);

    /**
     * Extension 4b: the radius is outside the allowed range. Keep the previous result.
     *
     * @param errorMessage the rule that was broken
     */
    void prepareInvalidRadiusView(String errorMessage);
}
