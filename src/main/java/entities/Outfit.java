package entities;

import java.util.List;
/**
 * The Outfit entity represents a reusable outfit configuration
 * saved by the user. It is part of the Entity layer in Clean Architecture,
 * meaning it contains only business-level data and no dependencies on
 * frameworks, UI, or use case logic.
 *
 * Entities are pure data containers and should not depend on application
 * logic. They represent the fundamental concepts in the domain of the app.
 */

public class Outfit {
    /** The user-assigned name of the outfit (e.g., "Snow Day Fit"). */
    private final String name;

    /** A list of clothing items included in the outfit (e.g., ["coat", "scarf"]). */
    private final List<String> items;


    /** Weather conditions for which the outfit is appropriate (e.g., "cold & windy"). */
    private final String weatherProfile;


    /** Optional: a location (city) associated with the outfit (can be empty ""). */
    private final String location;

    /**
     * Construct a new Outfit entity.
     *
     * @param name            the name of the outfit
     * @param items           the clothing items inside the outfit
     * @param weatherProfile  description of weather conditions
     * @param location        optional city name
     */
    public Outfit(String name, List<String> items,
                  String weatherProfile, String location) {
        this.name = name;
        this.items = items;
        this.weatherProfile = weatherProfile;
        this.location = location;
    }


    // Getters (Entities should be immutable)
    public String getName() {
        return name;
    }

    public List<String> getItems() {
        return items;
    }

    public String getWeatherProfile() {
        return weatherProfile;
    }

    public String getLocation() {
        return location;
    }
}
