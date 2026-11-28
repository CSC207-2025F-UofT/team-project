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

class AgeRangeFilterTest {

    private Pet petAged(int years) {
        return new Pet("1", "Miso", Species.CAT, "Tabby", years, PetSex.FEMALE, List.of(), "", false, true, true,
                BigDecimal.TEN, "", PetStatus.AVAILABLE);
    }

    @Test
    void keepsPetsInsideTheRange() {
        assertTrue(new AgeRangeFilter(1, 3).matches(petAged(2)));
    }

    @Test
    void rangeEndsAreIncluded() {
        final AgeRangeFilter filter = new AgeRangeFilter(1, 3);

        assertTrue(filter.matches(petAged(1)));
        assertTrue(filter.matches(petAged(3)));
    }

    @Test
    void dropsPetsOutsideTheRange() {
        final AgeRangeFilter filter = new AgeRangeFilter(1, 3);

        assertFalse(filter.matches(petAged(0)));
        assertFalse(filter.matches(petAged(4)));
    }

    @Test
    void rejectsBackwardsRange() {
        assertThrows(IllegalArgumentException.class, () -> new AgeRangeFilter(5, 2));
    }

    @Test
    void rejectsNegativeAge() {
        assertThrows(IllegalArgumentException.class, () -> new AgeRangeFilter(-1, 2));
    }
}
