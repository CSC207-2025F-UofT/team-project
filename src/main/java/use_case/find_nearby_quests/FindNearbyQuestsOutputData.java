package use_case.find_nearby_quests;

import java.util.List;

/**
 * Output of a successful search: the search circle and the quests inside it, nearest first.
 *
 * <p>Uses plain numbers instead of entities so outer layers never depend on the entity layer.
 */
public class FindNearbyQuestsOutputData {
    private final double centreLatitude;
    private final double centreLongitude;
    private final double radiusKm;
    private final double northEdgeLatitude;
    private final double eastEdgeLongitude;
    private final List<NearbyQuest> quests;

    /**
     * Creates the output data.
     *
     * @param centreLatitude latitude of the user's location
     * @param centreLongitude longitude of the user's location
     * @param radiusKm the radius that was searched, in kilometres
     * @param northEdgeLatitude latitude of the circle's edge straight north of the centre
     * @param eastEdgeLongitude longitude of the circle's edge straight east of the centre
     * @param quests the nearby quests, nearest first
     */
    public FindNearbyQuestsOutputData(double centreLatitude, double centreLongitude,
                                      double radiusKm, double northEdgeLatitude,
                                      double eastEdgeLongitude, List<NearbyQuest> quests) {
        this.centreLatitude = centreLatitude;
        this.centreLongitude = centreLongitude;
        this.radiusKm = radiusKm;
        this.northEdgeLatitude = northEdgeLatitude;
        this.eastEdgeLongitude = eastEdgeLongitude;
        this.quests = List.copyOf(quests);
    }

    /**
     * Returns the latitude of the user's location.
     *
     * @return the latitude
     */
    public double getCentreLatitude() {
        return centreLatitude;
    }

    /**
     * Returns the longitude of the user's location.
     *
     * @return the longitude
     */
    public double getCentreLongitude() {
        return centreLongitude;
    }

    /**
     * Returns the radius that was searched.
     *
     * @return the radius in kilometres
     */
    public double getRadiusKm() {
        return radiusKm;
    }

    /**
     * Returns the latitude of the circle's edge straight north of the centre.
     *
     * @return the latitude
     */
    public double getNorthEdgeLatitude() {
        return northEdgeLatitude;
    }

    /**
     * Returns the longitude of the circle's edge straight east of the centre.
     *
     * @return the longitude
     */
    public double getEastEdgeLongitude() {
        return eastEdgeLongitude;
    }

    /**
     * Returns the nearby quests, nearest first.
     *
     * @return an unmodifiable list
     */
    public List<NearbyQuest> getQuests() {
        return quests;
    }
}
