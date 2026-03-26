package api.googlePlacesAPI;

import entity.placeSuggestions.PlaceSuggestion;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GooglePlacesFetcherTest {

    @Test
    void testGetPlace_validPlace() throws PlaceFetcher.PlaceNotFoundException {
        GooglePlacesFetcher fetcher = new GooglePlacesFetcher();
        List<PlaceSuggestion> result = fetcher.getPlace("Tor");

        // Check that the result is not null
        assertNotNull(result, "Result should not be null");

        // Check that the list has at least one suggestion
        assertFalse(result.isEmpty(), "Result should contain at least one place suggestion");


        assertNotNull(result.get(0).getMainText(), "Place name should not be null");
        assertTrue(result.get(0).getMainText().toLowerCase().contains("tor"), "First place name should contain 'tor'");

    }
}
