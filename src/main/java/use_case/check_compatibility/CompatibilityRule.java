package use_case.check_compatibility;

import entity.AdopterPreferences;
import entity.Pet;

/**
 * One check of a cat against the adopter's situation. New rules are new classes.
 */
public interface CompatibilityRule {

    /**
     * Compares one trait of the cat with what the adopter said about their home.
     *
     * @param pet the cat being viewed
     * @param preferences what the adopter saved about their home
     * @return the outcome with a short explanation
     */
    RuleResult evaluate(Pet pet, AdopterPreferences preferences);
}
