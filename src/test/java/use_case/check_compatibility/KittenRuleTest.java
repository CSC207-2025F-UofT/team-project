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

class KittenRuleTest {
    private final KittenRule rule = new KittenRule();

    private Pet pet(int age) {
        return new Pet("1", "Kit", Species.CAT, "Tabby", age, PetSex.FEMALE, List.of(), "", false,
                true, true, BigDecimal.TEN, "", PetStatus.AVAILABLE);
    }

    private AdopterPreferences prefs(boolean canHandleKitten) {
        return new AdopterPreferences(false, false, false, canHandleKitten, false, null);
    }

    @Test
    void anAdultCatIsNotApplicable() {
        assertEquals(RuleResult.Status.NOT_APPLICABLE, rule.evaluate(pet(1), prefs(false)).getStatus());
    }

    @Test
    void aKittenAndAdopterCanHandleItIsAMatch() {
        final RuleResult result = rule.evaluate(pet(0), prefs(true));

        assertEquals(RuleResult.Status.MATCH, result.getStatus());
        assertEquals("Kitten care", result.getTrait());
    }

    @Test
    void aKittenAndAdopterCannotHandleItIsAConflict() {
        assertEquals(RuleResult.Status.CONFLICT, rule.evaluate(pet(0), prefs(false)).getStatus());
    }
}
