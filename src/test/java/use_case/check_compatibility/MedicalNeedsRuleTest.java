package use_case.check_compatibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.AdopterPreferences;
import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;

class MedicalNeedsRuleTest {
    private final MedicalNeedsRule rule = new MedicalNeedsRule();

    private Pet pet(boolean needsMedication) {
        return new Pet("1", "Miso", Species.CAT, "Tabby", 2, PetSex.FEMALE, List.of(), "", needsMedication,
                true, true, BigDecimal.TEN, "", PetStatus.AVAILABLE);
    }

    private AdopterPreferences prefs(boolean canHandleMedicalNeeds) {
        return new AdopterPreferences(false, false, canHandleMedicalNeeds, false, false, null);
    }

    @Test
    void noMedicationNeededIsNotApplicable() {
        assertEquals(RuleResult.Status.NOT_APPLICABLE, rule.evaluate(pet(false), prefs(false)).getStatus());
    }

    @Test
    void medicationNeededAndAdopterCanHandleItIsAMatch() {
        final RuleResult result = rule.evaluate(pet(true), prefs(true));

        assertEquals(RuleResult.Status.MATCH, result.getStatus());
        assertEquals("Medical needs", result.getTrait());
    }

    @Test
    void medicationNeededAndAdopterCannotHandleItIsAConflict() {
        final RuleResult result = rule.evaluate(pet(true), prefs(false));

        assertEquals(RuleResult.Status.CONFLICT, result.getStatus());
        assertTrue(result.getExplanation().contains("cannot take this on"));
    }
}
