package use_case.find_nearby_quests;

/** Input for finding nearby quests: the search radius the user picked. */
public class FindNearbyQuestsInputData {
    private final double radiusKm;

    /**
     * Creates the input data.
     *
     * @param radiusKm the search radius in kilometres
     */
    public FindNearbyQuestsInputData(double radiusKm) {
        this.radiusKm = radiusKm;
    }

    /**
     * Returns the search radius.
     *
     * @return the radius in kilometres
     */
    public double getRadiusKm() {
        return radiusKm;
    }
}
