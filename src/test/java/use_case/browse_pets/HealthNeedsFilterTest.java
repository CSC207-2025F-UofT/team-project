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

class HealthNeedsFilterTest {

    private Pet pet(boolean needsMedication) {
        return new Pet("1", "Miso", Species.CAT, "Tabby", 2, PetSex.FEMALE, List.of(), "", needsMedication,
                true, true, BigDecimal.TEN, "", PetStatus.AVAILABLE);
    }

    @Test
    void noMedicalNeedsKeepsOnlyHealthyPets() {
        final HealthNeedsFilter filter = new HealthNeedsFilter(false);

        assertTrue(filter.matches(pet(false)));
        assertFalse(filter.matches(pet(true)));
    }

    @Test
    void medicalNeedsKeepsOnlyPetsOnMedication() {
        final HealthNeedsFilter filter = new HealthNeedsFilter(true);

        assertTrue(filter.matches(pet(true)));
        assertFalse(filter.matches(pet(false)));
    }
}
