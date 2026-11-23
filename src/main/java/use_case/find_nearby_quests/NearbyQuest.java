package use_case.find_nearby_quests;

import java.util.List;

/** Everything the map needs about one nearby quest: its pin position and its preview. */
public class NearbyQuest {
    private final String questId;
    private final String title;
    private final List<String> interests;
    private final int xp;
    private final double distanceKm;
    private final double latitude;
    private final double longitude;

    /**
     * Creates a nearby quest.
     *
     * @param questId identifies the quest when the user opens it
     * @param title the quest's title
     * @param interests the quest's interests, shown as its theme
     * @param xp the XP the quest gives
     * @param distanceKm distance from the user, in kilometres
     * @param latitude latitude of the quest
     * @param longitude longitude of the quest
     */
    public NearbyQuest(String questId, String title, List<String> interests, int xp,
                       double distanceKm, double latitude, double longitude) {
        this.questId = questId;
        this.title = title;
        this.interests = List.copyOf(interests);
        this.xp = xp;
        this.distanceKm = distanceKm;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the quest's id.
     *
     * @return the id
     */
    public String getQuestId() {
        return questId;
    }

    /**
     * Returns the quest's title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the quest's interests.
     *
     * @return an unmodifiable list
     */
    public List<String> getInterests() {
        return interests;
    }

    /**
     * Returns the XP the quest gives.
     *
     * @return the XP
     */
    public int getXp() {
        return xp;
    }

    /**
     * Returns the distance from the user.
     *
     * @return the distance in kilometres
     */
    public double getDistanceKm() {
        return distanceKm;
    }

    /**
     * Returns the quest's latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the quest's longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }
}
