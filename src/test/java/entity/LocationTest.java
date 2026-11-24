package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class LocationTest {
    private static final double TOLERANCE_KM = 0.1;
    private static final Location UOFT = new Location(43.6629, -79.3957);
    private static final Location CN_TOWER = new Location(43.6426, -79.3871);

    @Test
    void storesCoordinates() {
        Location location = new Location(43.6629, -79.3957);
        assertEquals(43.6629, location.getLatitude());
        assertEquals(-79.3957, location.getLongitude());
    }

    @Test
    void acceptsBoundaryCoordinates() {
        new Location(90, 180);
        new Location(-90, -180);
    }

    @Test
    void rejectsLatitudeOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> new Location(90.01, 0));
        assertThrows(IllegalArgumentException.class, () -> new Location(-90.01, 0));
    }

    @Test
    void rejectsLongitudeOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> new Location(0, 180.01));
        assertThrows(IllegalArgumentException.class, () -> new Location(0, -180.01));
    }

    @Test
    void rejectsNaN() {
        assertThrows(IllegalArgumentException.class, () -> new Location(Double.NaN, 0));
        assertThrows(IllegalArgumentException.class, () -> new Location(0, Double.NaN));
    }

    @Test
    void distanceToSelfIsZero() {
        assertEquals(0.0, UOFT.distanceTo(UOFT));
    }

    @Test
    void distanceIsSymmetric() {
        assertEquals(UOFT.distanceTo(CN_TOWER), CN_TOWER.distanceTo(UOFT), 1e-9);
    }

    @Test
    void distanceMatchesKnownPair() {
        assertEquals(2.36, UOFT.distanceTo(CN_TOWER), TOLERANCE_KM);
    }

    @Test
    void equalLocationsAreEqual() {
        assertEquals(new Location(1, 2), new Location(1, 2));
        assertEquals(new Location(1, 2).hashCode(), new Location(1, 2).hashCode());
    }
}
