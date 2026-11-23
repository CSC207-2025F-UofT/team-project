package use_case.browse_pets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;
import entity.Temperament;

class TemperamentFilterTest {

    private Pet petWith(Temperament... tags) {
        return new Pet("1", "Miso", Species.CAT, "Tabby", 2, PetSex.FEMALE, List.of(tags), "", false, true, true,
                BigDecimal.TEN, "", PetStatus.AVAILABLE);
    }

    @Test
    void keepsPetWithThePickedTag() {
        final TemperamentFilter filter = new TemperamentFilter(Set.of(Temperament.CALM));

        assertTrue(filter.matches(petWith(Temperament.CALM, Temperament.SHY)));
    }

    @Test
    void dropsPetWithoutThePickedTag() {
        final TemperamentFilter filter = new TemperamentFilter(Set.of(Temperament.PLAYFUL));

        assertFalse(filter.matches(petWith(Temperament.CALM)));
    }

    @Test
    void petMustHaveEveryPickedTag() {
        final TemperamentFilter filter = new TemperamentFilter(
                Set.of(Temperament.CALM, Temperament.GOOD_WITH_KIDS));

        assertFalse(filter.matches(petWith(Temperament.CALM)));
        assertTrue(filter.matches(petWith(Temperament.CALM, Temperament.GOOD_WITH_KIDS)));
    }

    @Test
    void pickingNothingKeepsEveryPet() {
        assertTrue(new TemperamentFilter(Set.of()).matches(petWith()));
    }
}
