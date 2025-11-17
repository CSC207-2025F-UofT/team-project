package data_access;

import entity.Restaurant;
import java.util.ArrayList;
import java.util.List;

/**
 * A minimal implementation of RestaurantSearchService for testing purposes.
 *
 * Use this during development/testing to avoid API rate limits.
 */
public class RestaurantSearchServiceForLocalTesting implements RestaurantSearchService {
    private int callCount = 0;

    @Override
    public List<Restaurant> searchRestaurants(float latitude, float longitude, String term, int limit)
            throws RestaurantSearchException {
        callCount++;

        List<Restaurant> restaurants = new ArrayList<>();

        // Simulate search results for Toronto area (roughly)
        if (latitude > 43.0f && latitude < 44.0f && longitude < -79.0f && longitude > -80.0f) {

            // Mock restaurant 1: Italian restaurant
            Restaurant r1 = new Restaurant(
                    2.0f,  // $$
                    List.of(43.6629f, -79.3957f),
                    "Italian"
            );
            r1.addToRating(4);
            r1.addToRating(5);
            r1.addToRating(4);
            restaurants.add(r1);

            // Mock restaurant 2: Pizza place
            Restaurant r2 = new Restaurant(
                    1.0f,  // $
                    List.of(43.6650f, -79.4000f),
                    "Pizza"
            );
            r2.addToRating(3);
            r2.addToRating(4);
            restaurants.add(r2);

            // Mock restaurant 3: Sushi restaurant
            Restaurant r3 = new Restaurant(
                    3.0f,  // $$$
                    List.of(43.6700f, -79.3900f),
                    "Sushi"
            );
            r3.addToRating(5);
            r3.addToRating(5);
            r3.addToRating(4);
            restaurants.add(r3);

            // Mock restaurant 4: Burger joint
            Restaurant r4 = new Restaurant(
                    2.0f,  // $$
                    List.of(43.6600f, -79.3850f),
                    "American"
            );
            r4.addToRating(4);
            restaurants.add(r4);

            // Mock restaurant 5: Thai restaurant
            Restaurant r5 = new Restaurant(
                    2.0f,  // $$
                    List.of(43.6720f, -79.3970f),
                    "Thai"
            );
            r5.addToRating(4);
            r5.addToRating(5);
            restaurants.add(r5);

            // Return only up to the requested limit
            return restaurants.subList(0, Math.min(limit, restaurants.size()));
        }

        // If not in Toronto area, throw exception
        throw new RestaurantSearchException("No restaurants found in this area");
    }

    @Override
    public Restaurant getRestaurantDetails(String restaurantId) throws RestaurantSearchException {
        callCount++;

        // Simulate getting details for known restaurant IDs
        if ("test123".equals(restaurantId)) {
            Restaurant r = new Restaurant(2.0f, List.of(43.6629f, -79.3957f), "Italian");
            r.addToRating(4);
            r.addToRating(5);
            return r;
        }

        if ("pizza456".equals(restaurantId)) {
            Restaurant r = new Restaurant(1.0f, List.of(43.6650f, -79.4000f), "Pizza");
            r.addToRating(3);
            return r;
        }

        throw new RestaurantSearchException("Restaurant not found: " + restaurantId);
    }

    /**
     * Get the number of times this service was called.
     * @return number of calls made
     */
    public int getCallCount() {
        return callCount;
    }

    /**
     * Reset the call counter.
     */
    public void resetCallCount() {
        this.callCount = 0;
    }
}
