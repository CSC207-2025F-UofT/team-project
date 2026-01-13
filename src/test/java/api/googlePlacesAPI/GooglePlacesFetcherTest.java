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
        List<PlaceSuggestion> result = fetcher.getPlace("''");
        System.out.println(result);
    }
}
