package org.example.cocktailcraft.search;

import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.Tags;
import org.example.cocktailcraft.entity.Tags.AlcoholRange;
import org.example.cocktailcraft.entity.Tags.BaseSpirit;
import org.example.cocktailcraft.entity.Tags.Flavor;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearchClassificationTest {
    @Test
    void alcoholRangesHaveExplicitNonOverlappingBoundaries() {
        assertEquals(AlcoholRange.UNKNOWN, AlcoholRange.fromAbv(null));
        assertEquals(AlcoholRange.ZERO, AlcoholRange.fromAbv(0.0));
        assertEquals(AlcoholRange.LOW, AlcoholRange.fromAbv(0.01));
        assertEquals(AlcoholRange.LOW, AlcoholRange.fromAbv(9.99));
        assertEquals(AlcoholRange.MEDIUM, AlcoholRange.fromAbv(10.0));
        assertEquals(AlcoholRange.MEDIUM, AlcoholRange.fromAbv(19.99));
        assertEquals(AlcoholRange.HIGH, AlcoholRange.fromAbv(20.0));
        assertEquals(AlcoholRange.HIGH, AlcoholRange.fromAbv(100.0));
    }

    @Test
    void rejectsInvalidAlcoholPercentages() {
        for (Double abv : List.of(-1.0, 100.01, Double.NaN, Double.POSITIVE_INFINITY)) {
            assertThrows(IllegalArgumentException.class, () -> AlcoholRange.fromAbv(abv));
            assertThrows(IllegalArgumentException.class, () ->
                    new Cocktail("id", "name", List.of(), new Tags(Set.of()), "steps", abv));
        }
    }

    @Test
    void existingConstructorsPreserveInstructionsAndTreatAbvAsUnknown() {
        Cocktail legacy = new Cocktail("id", "name", List.of(), new Tags(Set.of(BaseSpirit.GIN)));
        Cocktail withSteps = new Cocktail("id", "name", List.of(), legacy.getTags(), "Stir.");
        assertNull(legacy.getAbv());
        assertEquals(AlcoholRange.UNKNOWN, legacy.getAlcoholRange());
        assertEquals(Set.of(), legacy.getTags().getFlavors());
        assertEquals("Stir.", withSteps.getInstructions());
        assertEquals(AlcoholRange.UNKNOWN, withSteps.getAlcoholRange());
        Cocktail known = new Cocktail("id", "name", List.of(), legacy.getTags(), "Stir.", 12.5);
        assertEquals(12.5, known.getAbv());
        assertEquals(AlcoholRange.MEDIUM, known.getAlcoholRange());
    }

    @Test
    void flavorAndSpiritSetsCannotBeChangedThroughCallers() {
        Set<BaseSpirit> spirits = EnumSet.of(BaseSpirit.GIN);
        Set<Flavor> flavors = EnumSet.of(Flavor.BITTER);
        Tags tags = new Tags(spirits, flavors);
        spirits.clear();
        flavors.clear();
        assertEquals(Set.of(BaseSpirit.GIN), tags.getBaseSpirits());
        assertEquals(Set.of(Flavor.BITTER), tags.getFlavors());
        assertThrows(UnsupportedOperationException.class, () -> tags.getFlavors().clear());
        assertThrows(UnsupportedOperationException.class, () -> tags.getBaseSpirits().clear());
    }
}
