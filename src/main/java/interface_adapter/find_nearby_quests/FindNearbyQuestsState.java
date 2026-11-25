package interface_adapter.find_nearby_quests;

import java.util.ArrayList;
import java.util.List;

/**
 * What the map view shows: the "you are here" position, the radius, the pins,
 * and any message. Every value is ready to display.
 */
public class FindNearbyQuestsState {
    private double centreLatitude;
    private double centreLongitude;
    private boolean hasCentre;
    private double radiusKm;
    private boolean hasCircle;
    private double circleNorthLatitude;
    private double circleEastLongitude;
    private List<PinState> pins = new ArrayList<>();
    private String infoMessage;
    private String errorMessage;
    private boolean retryAvailable;

    /** One quest pin and the text for its preview. */
    public static class PinState {
        private final String questId;
        private final String title;
        private final String theme;
        private final String xpText;
        private final String distanceText;
        private final double latitude;
        private final double longitude;

        /**
         * Creates a pin.
         *
         * @param questId identifies the quest to open
         * @param title the preview title
         * @param theme the preview theme text
         * @param xpText the preview XP text, e.g. "50 XP"
         * @param distanceText the preview distance text, e.g. "1.3 km"
         * @param latitude where to draw the pin
         * @param longitude where to draw the pin
         */
        public PinState(String questId, String title, String theme, String xpText,
                        String distanceText, double latitude, double longitude) {
            this.questId = questId;
            this.title = title;
            this.theme = theme;
            this.xpText = xpText;
            this.distanceText = distanceText;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        /**
         * Returns the id of the quest this pin opens.
         *
         * @return the quest id
         */
        public String getQuestId() {
            return questId;
        }

        /**
         * Returns the preview title.
         *
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * Returns the preview theme text.
         *
         * @return the theme
         */
        public String getTheme() {
            return theme;
        }

        /**
         * Returns the preview XP text.
         *
         * @return the XP text
         */
        public String getXpText() {
            return xpText;
        }

        /**
         * Returns the preview distance text.
         *
         * @return the distance text
         */
        public String getDistanceText() {
            return distanceText;
        }

        /**
         * Returns the pin latitude.
         *
         * @return the latitude
         */
        public double getLatitude() {
            return latitude;
        }

        /**
         * Returns the pin longitude.
         *
         * @return the longitude
         */
        public double getLongitude() {
            return longitude;
        }
    }

    /**
     * Returns the latitude of the "you are here" marker.
     *
     * @return the latitude
     */
    public double getCentreLatitude() {
        return centreLatitude;
    }

    /**
     * Returns the longitude of the "you are here" marker.
     *
     * @return the longitude
     */
    public double getCentreLongitude() {
        return centreLongitude;
    }

    /**
     * Returns whether a centre has been set yet.
     *
     * @return true once the use case has run at least once
     */
    public boolean hasCentre() {
        return hasCentre;
    }

    /**
     * Sets the "you are here" position.
     *
     * @param latitude the latitude
     * @param longitude the longitude
     */
    public void setCentre(double latitude, double longitude) {
        this.centreLatitude = latitude;
        this.centreLongitude = longitude;
        this.hasCentre = true;
    }

    /**
     * Returns the radius of the pins currently shown.
     *
     * @return the radius in kilometres
     */
    public double getRadiusKm() {
        return radiusKm;
    }

    /**
     * Sets the radius of the pins currently shown.
     *
     * @param radiusKm the radius in kilometres
     */
    public void setRadiusKm(double radiusKm) {
        this.radiusKm = radiusKm;
    }

    /**
     * Returns whether the search circle should be drawn.
     *
     * @return true after a successful search
     */
    public boolean hasCircle() {
        return hasCircle;
    }

    /**
     * Returns the latitude of the circle's north edge.
     *
     * @return the latitude
     */
    public double getCircleNorthLatitude() {
        return circleNorthLatitude;
    }

    /**
     * Returns the longitude of the circle's east edge.
     *
     * @return the longitude
     */
    public double getCircleEastLongitude() {
        return circleEastLongitude;
    }

    /**
     * Sets the search circle around the centre, given two points on its edge.
     *
     * @param northLatitude latitude of the edge straight north of the centre
     * @param eastLongitude longitude of the edge straight east of the centre
     */
    public void setCircle(double northLatitude, double eastLongitude) {
        this.circleNorthLatitude = northLatitude;
        this.circleEastLongitude = eastLongitude;
        this.hasCircle = true;
    }

    /** Removes the search circle, e.g. when quests could not be loaded. */
    public void clearCircle() {
        this.hasCircle = false;
    }

    /**
     * Returns the pins to draw.
     *
     * @return a copy of the pins
     */
    public List<PinState> getPins() {
        return new ArrayList<>(pins);
    }

    /**
     * Sets the pins to draw.
     *
     * @param pins the pins
     */
    public void setPins(List<PinState> pins) {
        this.pins = new ArrayList<>(pins);
    }

    /**
     * Returns an informational message, or null.
     *
     * @return the message
     */
    public String getInfoMessage() {
        return infoMessage;
    }

    /**
     * Sets an informational message.
     *
     * @param infoMessage the message, or null for none
     */
    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }

    /**
     * Returns an error message, or null.
     *
     * @return the message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets an error message.
     *
     * @param errorMessage the message, or null for none
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Returns whether the view should offer "Retry".
     *
     * @return true after a load failure
     */
    public boolean isRetryAvailable() {
        return retryAvailable;
    }

    /**
     * Sets whether the view should offer "Retry".
     *
     * @param retryAvailable true to offer it
     */
    public void setRetryAvailable(boolean retryAvailable) {
        this.retryAvailable = retryAvailable;
    }
}
