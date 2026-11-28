package use_case.check_compatibility;

import entity.AdopterPreferences;
import entity.Pet;

/**
 * Checks a kitten (under one year old) against whether the adopter can take on a kitten.
 */
public class KittenRule implements CompatibilityRule {
    private static final String TRAIT = "Kitten care";

    @Override
    public RuleResult evaluate(Pet pet, AdopterPreferences preferences) {
        if (pet.getAgeYears() >= 1) {
            return new RuleResult(TRAIT, RuleResult.Status.NOT_APPLICABLE,
                    pet.getName() + " is not a kitten.");
        }
        if (preferences.canHandleKitten()) {
            return new RuleResult(TRAIT, RuleResult.Status.MATCH,
                    pet.getName() + " is a kitten, and you said you can take on a kitten.");
        }
        return new RuleResult(TRAIT, RuleResult.Status.CONFLICT,
                pet.getName() + " is a kitten: you said you cannot take on a kitten.");
    }
}
