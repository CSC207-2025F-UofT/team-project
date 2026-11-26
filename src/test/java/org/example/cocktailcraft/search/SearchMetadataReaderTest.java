package org.example.cocktailcraft.search;

import org.example.cocktailcraft.data_access.CocktailJsonReader;
import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.Tags.AlcoholRange;
import org.example.cocktailcraft.entity.Tags.BaseSpirit;
import org.example.cocktailcraft.entity.Tags.Flavor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchMetadataReaderTest {
    private final CocktailJsonReader reader = new CocktailJsonReader();

    @Test
    void groupsExplicitFlavorTagsAndDeduplicatesOverlappingLabels() throws IOException {
        Cocktail cocktail = fixture("classified");
        assertEquals(EnumSet.allOf(Flavor.class), cocktail.getTags().getFlavors());
        assertEquals(10.0, cocktail.getAbv());
        assertEquals(AlcoholRange.MEDIUM, cocktail.getAlcoholRange());
    }

    @Test
    void normalizesCaseWhitespaceAndKnownSpellingVariants() throws IOException {
        assertEquals(Set.of(Flavor.SMOKY_SPICY, Flavor.HERBAL_FLORAL, Flavor.SAVORY),
                fixture("aliases").getTags().getFlavors());
    }

    @Test
    void mapsEachFlavorFamilyFromActualSingleFlavorRecipes() throws IOException {
        Map<String, Flavor> examples = Map.of(
                "queen-of-the-rodeo", Flavor.SWEET,
                "kentucky-flyer", Flavor.SOUR_CITRUS,
                "mezcal-mule", Flavor.FRUITY_TROPICAL,
                "sea-legs", Flavor.SMOKY_SPICY,
                "the-coffee-house", Flavor.COFFEE_NUTTY,
                "algonquin", Flavor.SAVORY,
                "pegu-club", Flavor.DRY);
        for (Map.Entry<String, Flavor> example : examples.entrySet()) {
            Cocktail cocktail = reader.read("/bar-assistant/data/cocktails/" + example.getKey() + "/data.json");
            assertEquals(Set.of(example.getValue()), cocktail.getTags().getFlavors(), example.getKey());
        }
    }

    @Test
    void missingAndNullMetadataRemainUnknownWithoutInventingClassifications() throws IOException {
        for (String name : List.of("missing", "null")) {
            Cocktail cocktail = fixture(name);
            assertNull(cocktail.getAbv());
            assertEquals(AlcoholRange.UNKNOWN, cocktail.getAlcoholRange());
            assertTrue(cocktail.getTags().getFlavors().isEmpty());
        }
    }

    @Test
    void ignoresSeasonStyleAndUnknownTagsAndPreservesExplicitZeroAbv() throws IOException {
        Cocktail cocktail = fixture("non-flavor");
        assertTrue(cocktail.getTags().getFlavors().isEmpty());
        assertEquals(0.0, cocktail.getAbv());
        assertEquals(AlcoholRange.ZERO, cocktail.getAlcoholRange());
    }

    @Test
    void invalidMetadataIsReportedAsACatalogReadFailure() {
        for (String name : List.of("invalid-negative", "invalid-large", "invalid-text",
                "invalid-overflow", "invalid-tags")) {
            assertThrows(IOException.class, () -> fixture(name), name);
        }
    }

    @Test
    void retainsExistingRecipeDataWhileLoadingNewMetadata() throws IOException {
        Cocktail cocktail = reader.read("/bar-assistant/data/cocktails/negroni/data.json");
        assertEquals(25.15, cocktail.getAbv());
        assertEquals(AlcoholRange.HIGH, cocktail.getAlcoholRange());
        assertEquals(Set.of(Flavor.BITTER), cocktail.getTags().getFlavors());
        assertEquals(Set.of(BaseSpirit.GIN), cocktail.getTags().getBaseSpirits());
        assertEquals("Negroni", cocktail.getName());
        assertEquals(3, cocktail.getIngredients().size());
        assertFalse(cocktail.getInstructions().isBlank());
        Cocktail ginTonic = reader.read("/bar-assistant/data/cocktails/gin-tonic/data.json");
        assertEquals(AlcoholRange.MEDIUM, ginTonic.getAlcoholRange());
        assertTrue(ginTonic.getTags().getFlavors().isEmpty(), "Summer does not imply a flavor");
    }

    @Test
    void allBundledRecipesLoadAndAbvRangesCoverTheCatalog() throws IOException {
        List<Cocktail> cocktails = reader.readAll();
        assertEquals(663, cocktails.size());
        Map<AlcoholRange, Long> ranges = cocktails.stream().collect(
                Collectors.groupingBy(Cocktail::getAlcoholRange, Collectors.counting()));
        assertEquals(Map.of(AlcoholRange.ZERO, 12L, AlcoholRange.LOW, 79L,
                AlcoholRange.MEDIUM, 287L, AlcoholRange.HIGH, 283L, AlcoholRange.UNKNOWN, 2L), ranges);
        for (Flavor flavor : Flavor.values()) {
            assertTrue(cocktails.stream().anyMatch(cocktail -> cocktail.getTags().getFlavors().contains(flavor)),
                    flavor.name());
        }
    }

    private Cocktail fixture(String name) throws IOException {
        return reader.read("/fixtures/search/" + name + ".json");
    }
}
