package use_case.check_compatibility;

import java.util.Optional;

import entity.AdopterPreferences;

/**
 * Where an adopter's saved preferences are kept.
 */
public interface AdopterPreferencesDataAccessInterface {

    /**
     * Finds the saved preferences of an adopter.
     *
     * @param adopterId the adopter
     * @return the preferences, or empty if the adopter has not saved any
     */
    Optional<AdopterPreferences> findByAdopterId(String adopterId);

    /**
     * Saves the preferences of an adopter, replacing any saved before.
     *
     * @param adopterId the adopter
     * @param preferences what to save
     */
    void save(String adopterId, AdopterPreferences preferences);
}
