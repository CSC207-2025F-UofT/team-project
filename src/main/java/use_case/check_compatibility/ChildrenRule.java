package use_case.check_compatibility;

import entity.AdopterPreferences;
import entity.Pet;
import entity.Temperament;

/**
 * Checks a cat against whether the adopter has children at home.
 */
public class ChildrenRule implements CompatibilityRule {
    private static final String TRAIT = "Children at home";

    @Override
    public RuleResult evaluate(Pet pet, AdopterPreferences preferences) {
        if (!preferences.hasChildren()) {
            return new RuleResult(TRAIT, RuleResult.Status.NOT_APPLICABLE,
                    "You have no children at home, so this does not matter.");
        }
        if (pet.getTemperamentTags().contains(Temperament.GOOD_WITH_KIDS)) {
            return new RuleResult(TRAIT, RuleResult.Status.MATCH,
                    pet.getName() + " is good with kids.");
        }
        return new RuleResult(TRAIT, RuleResult.Status.CONFLICT,
                pet.getName() + " is not listed as good with kids, and you have children at home.");
    }
}
