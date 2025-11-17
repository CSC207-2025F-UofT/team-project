package data_access;

import config.ConfigManager;
import entity.Restaurant;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * RestaurantSearchService implementation using Yelp API via RapidAPI.
 *
 * API Documentation: https://rapidapi.com/serg.osipchuk/api/YelpAPI
 *
 * Use this implementation in production. During development/testing, consider
 * using RestaurantSearchServiceForLocalTesting to avoid API rate limits.
 */
public class YelpRestaurantSearchService implements RestaurantSearchService {
    private final OkHttpClient client;
    private final String apiHost;
    private final String apiKey;

    /**
     * Constructor that loads configuration from ConfigManager.
     */
    public YelpRestaurantSearchService() {
        this.client = new OkHttpClient();
        ConfigManager.loadConfig();
        this.apiHost = ConfigManager.getYelpHost();
        this.apiKey = ConfigManager.getRapidApiKey();

        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("WARNING: RapidAPI key not configured");
            System.err.println("Please set rapidapi.key in config.properties");
        }
    }

    @Override
    public List<Restaurant> searchRestaurants(float latitude, float longitude, String term, int limit)
            throws RestaurantSearchException {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RestaurantSearchException("API key not configured");
        }

        Response response = null;
        try {
            // Encode search term
            String encodedTerm = java.net.URLEncoder.encode(term, "UTF-8");

            // Build URL
            String url = String.format(
                    "https://%s/businesses/search?latitude=%f&longitude=%f&term=%s&limit=%d&sort_by=best_match",
                    apiHost, latitude, longitude, encodedTerm, Math.min(limit, 50)
            );

            // Build request
            Request request = new Request.Builder()
                    .url(url)
                    .header("x-rapidapi-host", apiHost)
                    .header("x-rapidapi-key", apiKey)
                    .build();

            // Execute request
            response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new RestaurantSearchException("API returned status: " + response.code());
            }

            // Parse response
            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);

            List<Restaurant> restaurants = new ArrayList<>();

            if (jsonObject.has("businesses")) {
                JSONArray businesses = jsonObject.getJSONArray("businesses");

                for (int i = 0; i < businesses.length(); i++) {
                    Restaurant restaurant = parseRestaurantFromBusiness(businesses.getJSONObject(i));
                    restaurants.add(restaurant);
                }
            }

            if (restaurants.isEmpty()) {
                throw new RestaurantSearchException("No restaurants found");
            }

            return restaurants;

        } catch (IOException e) {
            throw new RestaurantSearchException("API call failed: " + e.getMessage());
        } catch (JSONException e) {
            throw new RestaurantSearchException("Error parsing JSON response: " + e.getMessage());
        } catch (RestaurantSearchException e) {
            throw e; // Re-throw our custom exception
        } catch (Exception e) {
            throw new RestaurantSearchException("Unexpected error: " + e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public Restaurant getRestaurantDetails(String restaurantId) throws RestaurantSearchException {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RestaurantSearchException("API key not configured");
        }

        Response response = null;
        try {
            // Build URL
            String url = "https://" + apiHost + "/businesses/details?business_id=" + restaurantId;

            // Build request
            Request request = new Request.Builder()
                    .url(url)
                    .header("x-rapidapi-host", apiHost)
                    .header("x-rapidapi-key", apiKey)
                    .build();

            // Execute request
            response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new RestaurantSearchException("API returned status: " + response.code());
            }

            // Parse response
            String responseBody = response.body().string();
            JSONObject business = new JSONObject(responseBody);

            return parseRestaurantFromBusiness(business);

        } catch (IOException e) {
            throw new RestaurantSearchException("API call failed: " + e.getMessage());
        } catch (JSONException e) {
            throw new RestaurantSearchException("Error parsing JSON response: " + e.getMessage());
        } catch (Exception e) {
            throw new RestaurantSearchException("Unexpected error: " + e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * Helper method to parse a Restaurant from a Yelp business JSON object.
     * Extracts all required fields including id, name, address, etc.
     *
     * @param business JSON object from Yelp API
     * @return Restaurant entity with all fields populated
     * @throws JSONException if required fields are missing
     */
    private Restaurant parseRestaurantFromBusiness(JSONObject business) throws JSONException {
        // Extract required data
        String id = business.getString("id");
        String name = business.getString("name");
        float rating = business.optFloat("rating", 0.0f);
        String priceLevel = business.optString("price", "$");

        // Get address information
        String fullAddress = "";
        String zipCode = "";

        if (business.has("location")) {
            JSONObject location = business.getJSONObject("location");
            zipCode = location.optString("zip_code", "");

            // Prefer display_address if available (already formatted by Yelp)
            if (location.has("display_address")) {
                JSONArray displayAddress = location.getJSONArray("display_address");
                if (displayAddress.length() > 0) {
                    StringBuilder displayBuilder = new StringBuilder();
                    for (int j = 0; j < displayAddress.length(); j++) {
                        if (j > 0) displayBuilder.append(", ");
                        displayBuilder.append(displayAddress.getString(j));
                    }
                    fullAddress = displayBuilder.toString();
                }
            }

            // Fallback: Build address from components if display_address not available
            if (fullAddress.isEmpty()) {
                String address1 = location.optString("address1", "");
                String address2 = location.optString("address2", "");
                String address3 = location.optString("address3", "");
                String city = location.optString("city", "");
                String state = location.optString("state", "");

                StringBuilder addressBuilder = new StringBuilder();
                if (!address1.isEmpty()) addressBuilder.append(address1);
                if (!address2.isEmpty()) {
                    if (addressBuilder.length() > 0) addressBuilder.append(" ");
                    addressBuilder.append(address2);
                }
                if (!address3.isEmpty()) {
                    if (addressBuilder.length() > 0) addressBuilder.append(" ");
                    addressBuilder.append(address3);
                }
                if (!city.isEmpty()) {
                    if (addressBuilder.length() > 0) addressBuilder.append(", ");
                    addressBuilder.append(city);
                }
                if (!state.isEmpty()) {
                    if (addressBuilder.length() > 0) addressBuilder.append(", ");
                    addressBuilder.append(state);
                }

                fullAddress = addressBuilder.toString();
            }
        }

        // If address is still empty, provide a default
        if (fullAddress.isEmpty()) {
            fullAddress = "Address not available";
        }

        // Get coordinates (REQUIRED - restaurants must have coordinates for distance calculation)
        List<Float> coordinates;
        if (business.has("coordinates")) {
            JSONObject coords = business.getJSONObject("coordinates");
            float lat = coords.getFloat("latitude");
            float lng = coords.getFloat("longitude");
            coordinates = List.of(lat, lng);
        } else {
            // Coordinates are essential - throw exception if missing
            throw new JSONException("Restaurant missing coordinates: " + name);
        }

        // Get categories for food type (use primary category)
        String foodType = "Restaurant";
        if (business.has("categories") && business.getJSONArray("categories").length() > 0) {
            JSONArray categories = business.getJSONArray("categories");
            foodType = categories.getJSONObject(0).getString("title");
        }

        // Convert price level to float ($ = 1.0, $$ = 2.0, etc.)
        // If empty/null, default to 1.0 ($)
        float priceRange = (priceLevel == null || priceLevel.isEmpty())
                ? 1.0f
                : Math.min(priceLevel.length(), 4);

        // Create Restaurant entity with ALL fields
        Restaurant restaurant = new Restaurant(
                id, name, fullAddress, zipCode,
                priceRange, coordinates, foodType
        );

        // Add Yelp rating as initial rating
        // Convert 0-5 float rating to 1-5 integer rating
        if (rating > 0) {
            int starRating = Math.round(rating);
            starRating = Math.max(1, Math.min(5, starRating)); // Ensure 1-5 range
            restaurant.addToRating(starRating);
        }

        return restaurant;
    }
}