package use_case.browse_pets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;

class SpeciesFilterTest {

    private Pet pet(Species species) {
        return new Pet("1", "Rex", species, "Mixed", 2, PetSex.MALE, List.of(), "", false, true, true,
                BigDecimal.TEN, "", PetStatus.AVAILABLE);
    }

    @Test
    void keepsPetsOfTheChosenSpecies() {
        assertTrue(new SpeciesFilter(Species.DOG).matches(pet(Species.DOG)));
    }

    @Test
    void dropsPetsOfOtherSpecies() {
        assertFalse(new SpeciesFilter(Species.DOG).matches(pet(Species.CAT)));
    }
}
