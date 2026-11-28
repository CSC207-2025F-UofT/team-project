package use_case.check_compatibility;

import entity.AdopterPreferences;
import entity.Pet;

/**
 * Checks a cat's medication needs against whether the adopter can take that on.
 */
public class MedicalNeedsRule implements CompatibilityRule {
    private static final String TRAIT = "Medical needs";

    @Override
    public RuleResult evaluate(Pet pet, AdopterPreferences preferences) {
        if (!pet.needsMedication()) {
            return new RuleResult(TRAIT, RuleResult.Status.NOT_APPLICABLE,
                    pet.getName() + " does not need daily medication.");
        }
        if (preferences.canHandleMedicalNeeds()) {
            return new RuleResult(TRAIT, RuleResult.Status.MATCH,
                    pet.getName() + " needs daily medication, and you said you can take that on.");
        }
        return new RuleResult(TRAIT, RuleResult.Status.CONFLICT,
                "Needs daily medication: you said you cannot take this on.");
    }
}
