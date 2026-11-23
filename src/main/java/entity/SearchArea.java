package entity;

/**
 * A circle on the map: a centre and a radius in kilometres.
 *
 * <p>Holds the rule for what counts as "nearby": a point is nearby when it is
 * inside the circle. The radius must be between {@link #MIN_RADIUS_KM} and
 * {@link #MAX_RADIUS_KM}.
 */
public class SearchArea {
    /** Smallest radius a user may search with, in kilometres. */
    public static final double MIN_RADIUS_KM = 0.5;
    /** Largest radius a user may search with, in kilometres. */
    public static final double MAX_RADIUS_KM = 10.0;
    /** Radius used the first time the map is opened, in kilometres. */
    public static final double DEFAULT_RADIUS_KM = 2.0;

    private final Location centre;
    private final double radiusKm;

    /**
     * Creates a search area.
     *
     * @param centre the centre of the circle
     * @param radiusKm the radius in kilometres
     * @throws IllegalArgumentException if centre is null or the radius is out of range
     */
    public SearchArea(Location centre, double radiusKm) {
        if (centre == null) {
            throw new IllegalArgumentException("A search area needs a centre.");
        }
        if (!(radiusKm >= MIN_RADIUS_KM && radiusKm <= MAX_RADIUS_KM)) {
            throw new IllegalArgumentException(
                    "Radius must be between " + MIN_RADIUS_KM + " and " + MAX_RADIUS_KM + " km.");
        }
        this.centre = centre;
        this.radiusKm = radiusKm;
    }

    /**
     * Returns the centre of the circle.
     *
     * @return the centre
     */
    public Location getCentre() {
        return centre;
    }

    /**
     * Returns the radius in kilometres.
     *
     * @return the radius
     */
    public double getRadiusKm() {
        return radiusKm;
    }

    /**
     * Returns the distance from the centre to a point.
     *
     * @param point the point
     * @return the distance in kilometres
     */
    public double distanceFromCentre(Location point) {
        return centre.distanceTo(point);
    }

    /**
     * Returns whether a point is inside the circle. A point exactly on the edge counts as inside.
     *
     * @param point the point to check
     * @return true if the point is at most radiusKm from the centre
     */
    public boolean contains(Location point) {
        return distanceFromCentre(point) <= radiusKm;
    }
}
