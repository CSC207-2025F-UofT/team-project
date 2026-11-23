package entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SearchAreaTest {
    private static final Location UOFT = new Location(43.6629, -79.3957);
    private static final Location CN_TOWER = new Location(43.6426, -79.3871);

    @Test
    void acceptsMinAndMaxRadius() {
        new SearchArea(UOFT, SearchArea.MIN_RADIUS_KM);
        new SearchArea(UOFT, SearchArea.MAX_RADIUS_KM);
    }

    @Test
    void rejectsRadiusOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> new SearchArea(UOFT, 0.49));
        assertThrows(IllegalArgumentException.class, () -> new SearchArea(UOFT, 10.01));
        assertThrows(IllegalArgumentException.class, () -> new SearchArea(UOFT, Double.NaN));
    }

    @Test
    void rejectsNullCentre() {
        assertThrows(IllegalArgumentException.class, () -> new SearchArea(null, 2));
    }

    @Test
    void containsPointInsideRadius() {
        assertTrue(new SearchArea(UOFT, 5).contains(CN_TOWER));
    }

    @Test
    void doesNotContainPointOutsideRadius() {
        assertFalse(new SearchArea(UOFT, 1).contains(CN_TOWER));
    }

    @Test
    void pointExactlyOnEdgeIsInside() {
        double edge = UOFT.distanceTo(CN_TOWER);
        // Only valid if the edge distance is itself an allowed radius.
        assertTrue(edge >= SearchArea.MIN_RADIUS_KM && edge <= SearchArea.MAX_RADIUS_KM);
        assertTrue(new SearchArea(UOFT, edge).contains(CN_TOWER));
    }
}
