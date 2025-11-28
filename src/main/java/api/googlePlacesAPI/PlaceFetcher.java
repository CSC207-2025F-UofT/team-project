package api.googlePlacesAPI;

import entity.placeSuggestions.PlaceSuggestion;
import org.json.JSONObject;

import java.util.List;

public interface PlaceFetcher {
    List<PlaceSuggestion> getPlace(String placeId) throws PlaceNotFoundException;

    class PlaceNotFoundException extends Exception {
        public PlaceNotFoundException(String placeId) {
            super("Place not found: " + placeId);
        }
    }


}
