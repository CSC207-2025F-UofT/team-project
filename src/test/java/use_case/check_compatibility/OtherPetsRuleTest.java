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
import entity.Temperament;

class OtherPetsRuleTest {
    private final OtherPetsRule rule = new OtherPetsRule();

    private Pet pet(Temperament... tags) {
        return new Pet("1", "Miso", Species.CAT, "Tabby", 2, PetSex.FEMALE, List.of(tags), "", false, true, true,
                BigDecimal.TEN, "", PetStatus.AVAILABLE);
    }

    private AdopterPreferences prefs(boolean hasOtherPets) {
        return new AdopterPreferences(false, hasOtherPets, false, false, false, null);
    }

    @Test
    void noOtherPetsMeansNotApplicable() {
        assertEquals(RuleResult.Status.NOT_APPLICABLE, rule.evaluate(pet(), prefs(false)).getStatus());
    }

    @Test
    void otherPetsAndGoodWithOtherPetsIsAMatch() {
        final RuleResult result = rule.evaluate(pet(Temperament.GOOD_WITH_OTHER_PETS), prefs(true));

        assertEquals(RuleResult.Status.MATCH, result.getStatus());
        assertEquals("Other pets at home", result.getTrait());
    }

    @Test
    void otherPetsAndNotGoodWithOtherPetsIsAConflict() {
        assertEquals(RuleResult.Status.CONFLICT, rule.evaluate(pet(Temperament.SHY), prefs(true)).getStatus());
    }
}
