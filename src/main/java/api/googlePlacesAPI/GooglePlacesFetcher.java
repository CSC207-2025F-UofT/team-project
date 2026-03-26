package api.googlePlacesAPI;

import entity.placeSuggestions.PlaceSuggestion;
import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.List;
import java.util.ArrayList;

import java.io.IOException;

public class GooglePlacesFetcher implements PlaceFetcher {
    private final OkHttpClient client = new OkHttpClient();

    // Loading token from environment variables
    public static String getAPIToken() {
        return System.getenv("GOOGLE_PLACES_KEY");
    }

    @Override
    public List<PlaceSuggestion> getPlace(String place) throws PlaceNotFoundException {
        final String API_KEY = getAPIToken();
        final String ADDRESS_COMPONENTS = place;

        // Checks if there is an API key in env variables
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new RuntimeException("Google Places API key not set in environment variable");
        }


        JSONObject bodyJson = new JSONObject();

        JSONArray typesArray = new JSONArray();
        typesArray.put("(cities)");

        bodyJson.put("input", ADDRESS_COMPONENTS);
        bodyJson.put("includedPrimaryTypes", typesArray);
        RequestBody body = RequestBody.create(
                bodyJson.toString(),
                MediaType.parse("application/json")
        );

        String API_URL = "https://places.googleapis.com/v1/places:autocomplete";
        final Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Goog-Api-Key", API_KEY)
                .build();

        try (final Response response = client.newCall(request).execute()) {
            final JSONObject responseBody = new JSONObject(response.body().string());
            if (response.code() == 200) {
                List<PlaceSuggestion> suggestions = parseSuggestions(responseBody);

                return suggestions;
            } else {
                throw new PlaceNotFoundException("Invalid JSON returned");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<PlaceSuggestion> parseSuggestions(JSONObject responseJson) {
        List<PlaceSuggestion> suggestions = new ArrayList<>();

        JSONArray suggestionsArray = responseJson.optJSONArray("suggestions");
        if (suggestionsArray == null) return suggestions; // no suggestions

        for (int i = 0; i < suggestionsArray.length(); i++) {
            JSONObject suggestionObj = suggestionsArray.getJSONObject(i)
                    .optJSONObject("placePrediction");
            if (suggestionObj == null) continue;

            // Get placeId
            String placeId = suggestionObj.optString("placeId", "");

            // Get mainText
            String mainText = "";
            JSONObject structuredFormat = suggestionObj.optJSONObject("structuredFormat");
            if (structuredFormat != null) {
                JSONObject mainTextObj = structuredFormat.optJSONObject("mainText");
                if (mainTextObj != null) {
                    mainText = mainTextObj.optString("text", "");
                }
            }

            // secondaryText may not exist in API response
            String secondaryText = "";
            JSONObject secondaryTextObj = structuredFormat != null ? structuredFormat.optJSONObject("secondaryText") : null;
            if (secondaryTextObj != null) {
                secondaryText = secondaryTextObj.optString("text", "");
            }

            suggestions.add(new PlaceSuggestion(placeId, mainText, secondaryText));
        }

        return suggestions;
    }
}
