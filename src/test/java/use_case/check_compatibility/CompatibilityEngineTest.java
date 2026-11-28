package use_case.check_compatibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.AdopterPreferences;
import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;

class CompatibilityEngineTest {
    private final Pet pet = new Pet("1", "Kit", Species.CAT, "Tabby", 0, PetSex.FEMALE, List.of(), "", true,
            true, true, BigDecimal.TEN, "", PetStatus.AVAILABLE);

    @Test
    void runsEveryRuleInOrder() {
        final CompatibilityRule first = (p, a) -> new RuleResult("First", RuleResult.Status.MATCH, "one");
        final CompatibilityRule second = (p, a) -> new RuleResult("Second", RuleResult.Status.CONFLICT, "two");

        final List<RuleResult> results = new CompatibilityEngine(List.of(first, second))
                .evaluate(pet, new AdopterPreferences(false, false, false, false, false, null));

        assertEquals(2, results.size());
        assertEquals("First", results.get(0).getTrait());
        assertEquals("Second", results.get(1).getTrait());
    }

    @Test
    void standardRulesFlagAKittenThatNeedsMedicationForAnAdopterWhoCannot() {
        final List<RuleResult> results = CompatibilityEngine.withStandardRules()
                .evaluate(pet, new AdopterPreferences(false, false, false, false, false, null));

        assertEquals(4, results.size());
        final long conflicts = results.stream().filter(r -> r.getStatus() == RuleResult.Status.CONFLICT).count();
        assertEquals(2, conflicts);
    }
}
