package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class PetTest {

    @Test
    void petKeepsAnImmutableCopyOfTemperamentTags() {
        final List<Temperament> tags = new ArrayList<>(List.of(Temperament.SHY));
        final Pet pet = createPet(" Mochi ", 0, BigDecimal.ZERO, tags);

        tags.add(Temperament.PLAYFUL);

        assertEquals("Mochi", pet.getName());
        assertEquals(0, pet.getAgeYears());
        assertEquals(BigDecimal.ZERO, pet.getAdoptionFee());
        assertEquals("", pet.getHealthNotes());
        assertEquals("", pet.getPhotoPath());
        assertEquals(List.of(Temperament.SHY), pet.getTemperamentTags());
        assertThrows(UnsupportedOperationException.class, () -> pet.getTemperamentTags().clear());
    }

    @Test
    void rejectsMissingNameAndNegativeAge() {
        assertThrows(IllegalArgumentException.class, () -> createPet(" ", 0, BigDecimal.ZERO, List.of()));
        assertThrows(IllegalArgumentException.class, () -> createPet("Mochi", -1, BigDecimal.ZERO, List.of()));
    }

    @Test
    void acceptsOnlyNonnegativeFeesInWholeCents() {
        final BigDecimal fee = new BigDecimal("125.200");
        final Pet pet = createPet("Mochi", 1, fee, List.of());

        assertEquals(fee, pet.getAdoptionFee());
        assertThrows(IllegalArgumentException.class,
                () -> createPet("Mochi", 1, new BigDecimal("-0.01"), List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> createPet("Mochi", 1, new BigDecimal("125.201"), List.of()));
    }

    private static Pet createPet(String name, int ageYears, BigDecimal fee, List<Temperament> tags) {
        return new Pet("pet1", name, Species.CAT, "Domestic shorthair", ageYears, PetSex.UNKNOWN, tags,
                null, false, true, true, fee, " ", PetStatus.AVAILABLE);
    }
}
