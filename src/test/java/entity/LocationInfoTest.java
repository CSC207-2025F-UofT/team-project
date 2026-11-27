package entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LocationInfoTest {
    private final LocationInfo toronto = new LocationInfo("Toronto", null, "Ontario", "Canada", "North America");
    private final LocationInfo markham = new LocationInfo("Markham", "York Region", "Ontario", "Canada", "North America");

    @Test
    void presentInfoIsCorrect() {
        assertEquals("Ontario", markham.getState());
    }

    @Test
    void missingInfoIsMissing() {
        assertEquals(null, toronto.getCounty());
    }

    @Test
    void generalInfoIsCorrect() {
        assertEquals("North America", markham.getType(LocationInfoType.CONTINENT));
    }

    @Test
    void displayStringIsCorrect() {
        assertEquals("Toronto, Ontario, Canada", toronto.getDisplayString());
    }
}
