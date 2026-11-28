package use_case.browse_pets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;

class MoreFiltersTest {

    private Pet pet(String breed, PetSex sex, boolean vaccinated, boolean spayedNeutered) {
        return new Pet("1", "Miso", Species.CAT, breed, 2, sex, List.of(), "", false, vaccinated, spayedNeutered,
                BigDecimal.TEN, "", PetStatus.AVAILABLE);
    }

    @Test
    void breedMatchesPartOfTheNameIgnoringCase() {
        final BreedFilter filter = new BreedFilter("  TAB ");

        assertTrue(filter.matches(pet("Tabby", PetSex.FEMALE, true, true)));
        assertFalse(filter.matches(pet("Siamese", PetSex.FEMALE, true, true)));
    }

    @Test
    void blankOrMissingBreedKeepsEveryPet() {
        assertTrue(new BreedFilter("").matches(pet("Siamese", PetSex.FEMALE, true, true)));
        assertTrue(new BreedFilter(null).matches(pet("Siamese", PetSex.FEMALE, true, true)));
    }

    @Test
    void sexKeepsOnlyThatSex() {
        final SexFilter filter = new SexFilter(PetSex.MALE);

        assertTrue(filter.matches(pet("Tabby", PetSex.MALE, true, true)));
        assertFalse(filter.matches(pet("Tabby", PetSex.FEMALE, true, true)));
    }

    @Test
    void sexIsRequired() {
        assertThrows(IllegalArgumentException.class, () -> new SexFilter(null));
    }

    @Test
    void vaccinatedKeepsOnlyVaccinatedPets() {
        assertTrue(new VaccinatedFilter().matches(pet("Tabby", PetSex.MALE, true, false)));
        assertFalse(new VaccinatedFilter().matches(pet("Tabby", PetSex.MALE, false, true)));
    }

    @Test
    void spayedNeuteredKeepsOnlyFixedPets() {
        assertTrue(new SpayedNeuteredFilter().matches(pet("Tabby", PetSex.MALE, false, true)));
        assertFalse(new SpayedNeuteredFilter().matches(pet("Tabby", PetSex.MALE, true, false)));
    }
}
