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
import entity.Temperament;

class ChildrenRuleTest {
    private final ChildrenRule rule = new ChildrenRule();

    private Pet pet(Temperament... tags) {
        return new Pet("1", "Miso", Species.CAT, "Tabby", 2, PetSex.FEMALE, List.of(tags), "", false, true, true,
                BigDecimal.TEN, "", PetStatus.AVAILABLE);
    }

    private AdopterPreferences prefs(boolean hasChildren) {
        return new AdopterPreferences(hasChildren, false, false, false, false, null);
    }

    @Test
    void noChildrenMeansNotApplicable() {
        final RuleResult result = rule.evaluate(pet(), prefs(false));

        assertEquals(RuleResult.Status.NOT_APPLICABLE, result.getStatus());
    }

    @Test
    void childrenAndGoodWithKidsIsAMatch() {
        final RuleResult result = rule.evaluate(pet(Temperament.GOOD_WITH_KIDS), prefs(true));

        assertEquals(RuleResult.Status.MATCH, result.getStatus());
        assertEquals("Children at home", result.getTrait());
    }

    @Test
    void childrenAndNotGoodWithKidsIsAConflict() {
        final RuleResult result = rule.evaluate(pet(Temperament.SHY), prefs(true));

        assertEquals(RuleResult.Status.CONFLICT, result.getStatus());
        assertTrue(result.getExplanation().contains("Miso"));
    }
}
