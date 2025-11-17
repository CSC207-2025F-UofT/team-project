package data_access;

import entity.Restaurant;
import java.util.*;

/**
 * This RestaurantSearchService caches search results to improve performance.
 * Similar to CachingBreedFetcher - wraps another RestaurantSearchService implementation.
 *
 * Benefits of caching:
 * - Reduces API calls (saves rate limit quota)
 * - Faster response times
 * - Good for repeated searches in same area
 *
 * If a call produces a RestaurantSearchException, it is NOT cached.
 */
public class CachingRestaurantSearchService implements RestaurantSearchService {
    private int callsMade = 0;
    private final RestaurantSearchService service;
    private final Map<String, List<Restaurant>> searchCache;
    private final Map<String, Restaurant> detailsCache;

    /**
     * Create a caching wrapper around any RestaurantSearchService implementation.
     * @param service the underlying service to cache (can be real API or test)
     */
    public CachingRestaurantSearchService(RestaurantSearchService service) {
        this.service = service;
        this.searchCache = new HashMap<>();
        this.detailsCache = new HashMap<>();
    }

    @Override
    public List<Restaurant> searchRestaurants(float latitude, float longitude, String term, int limit)
            throws RestaurantSearchException {
        // Create cache key from search parameters
        String cacheKey = String.format("%.4f_%.4f_%s_%d", latitude, longitude, term.toLowerCase(), limit);

        // Check cache first
        if (searchCache.containsKey(cacheKey)) {
            // Cache hit - return cached results
            return new ArrayList<>(searchCache.get(cacheKey)); // Return copy
        }

        // Cache miss - call underlying service
        callsMade++;
        List<Restaurant> restaurants = service.searchRestaurants(latitude, longitude, term, limit);

        // Cache successful results only
        searchCache.put(cacheKey, new ArrayList<>(restaurants)); // Store copy

        return restaurants;
    }

    @Override
    public Restaurant getRestaurantDetails(String restaurantId) throws RestaurantSearchException {
        // Normalize restaurant ID
        String cacheKey = restaurantId.toLowerCase().trim();

        // Check cache first
        if (detailsCache.containsKey(cacheKey)) {
            // Cache hit
            return detailsCache.get(cacheKey);
        }

        // Cache miss - call underlying service
        callsMade++;
        Restaurant restaurant = service.getRestaurantDetails(restaurantId);

        // Cache successful results only
        detailsCache.put(cacheKey, restaurant);

        return restaurant;
    }

    /**
     * Get the number of times the underlying service was actually called.
     * This does NOT include cache hits.
     * @return number of calls made to underlying service
     */
    public int getCallsMade() {
        return callsMade;
    }

    /**
     * Clear all cached search results.
     * Useful for testing or when you need fresh data.
     */
    public void clearCache() {
        searchCache.clear();
        detailsCache.clear();
    }

    /**
     * Get the total number of items in both caches.
     * @return total cache size
     */
    public int getCacheSize() {
        return searchCache.size() + detailsCache.size();
    }

    /**
     * Clear only search results cache.
     */
    public void clearSearchCache() {
        searchCache.clear();
    }

    /**
     * Clear only restaurant details cache.
     */
    public void clearDetailsCache() {
        detailsCache.clear();
    }
}