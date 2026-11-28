package use_case.check_compatibility;

import java.util.ArrayList;
import java.util.List;

import entity.AdopterPreferences;
import entity.Pet;

/**
 * Runs every compatibility rule on a cat. To check something new, add a rule; this class does not change.
 */
public class CompatibilityEngine {
    private final List<CompatibilityRule> rules;

    /**
     * Creates the engine.
     *
     * @param rules the rules to run, in the order their results should be shown
     */
    public CompatibilityEngine(List<CompatibilityRule> rules) {
        this.rules = List.copyOf(rules);
    }

    /**
     * Creates the engine with every rule the project has today.
     *
     * @return an engine with the children, other pets, medical needs and kitten rules
     */
    public static CompatibilityEngine withStandardRules() {
        return new CompatibilityEngine(List.of(new ChildrenRule(), new OtherPetsRule(),
                new MedicalNeedsRule(), new KittenRule()));
    }

    /**
     * Checks one cat against what the adopter said about their home.
     *
     * @param pet the cat being viewed
     * @param preferences what the adopter saved
     * @return one result per rule, in rule order
     */
    public List<RuleResult> evaluate(Pet pet, AdopterPreferences preferences) {
        final List<RuleResult> results = new ArrayList<>();
        for (CompatibilityRule rule : rules) {
            results.add(rule.evaluate(pet, preferences));
        }
        return results;
    }
}
