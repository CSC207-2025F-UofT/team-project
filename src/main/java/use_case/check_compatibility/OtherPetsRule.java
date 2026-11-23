package use_case.check_compatibility;

import entity.AdopterPreferences;
import entity.Pet;
import entity.Temperament;

/**
 * Checks a cat against whether the adopter has other pets at home.
 */
public class OtherPetsRule implements CompatibilityRule {
    private static final String TRAIT = "Other pets at home";

    @Override
    public RuleResult evaluate(Pet pet, AdopterPreferences preferences) {
        if (!preferences.hasOtherPets()) {
            return new RuleResult(TRAIT, RuleResult.Status.NOT_APPLICABLE,
                    "You have no other pets at home, so this does not matter.");
        }
        if (pet.getTemperamentTags().contains(Temperament.GOOD_WITH_OTHER_PETS)) {
            return new RuleResult(TRAIT, RuleResult.Status.MATCH,
                    pet.getName() + " gets along with other pets.");
        }
        return new RuleResult(TRAIT, RuleResult.Status.CONFLICT,
                pet.getName() + " is not listed as good with other pets, and you have other pets at home.");
    }
}
