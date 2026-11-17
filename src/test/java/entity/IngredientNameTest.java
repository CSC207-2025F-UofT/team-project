package entity;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IngredientNameTest {

    @Test
    void normalizesNameForComparisonTest() {
        IngredientName ingredient = IngredientName.fromRawName("  Chicken__Breast \t");

        assertEquals("chicken breast", ingredient.getKey());
        assertEquals("Chicken__Breast", ingredient.getDisplayName());
        assertEquals("Chicken__Breast", ingredient.toString());
    }

    @Test
    void normalizedNamesAreEqualTest() {
        IngredientName first = IngredientName.fromRawName("Chicken_Breast");
        IngredientName second = IngredientName.fromRawName("  chicken   breast  ");
        IngredientName third = IngredientName.fromRawName("CHICKEN BREAST");

        assertEquals(first, second);
        assertEquals(second, first);
        assertEquals(second, third);
        assertEquals(first, third);
        assertEquals(first.hashCode(), second.hashCode());

        Set<IngredientName> ingredients = new HashSet<>();
        ingredients.add(first);
        assertTrue(ingredients.contains(second));
    }

    @Test
    void differentNamesAreNotEqualTest() {
        IngredientName ingredient = IngredientName.fromRawName("Chicken Breast");

        assertNotEquals(ingredient, IngredientName.fromRawName("Chicken Thigh"));
        assertNotEquals("chicken breast", ingredient);
        assertNotEquals(null, ingredient);
        assertEquals(ingredient, ingredient);
    }

    @Test
    void rejectsNullNameTest() {
        assertThrows(IllegalArgumentException.class, () -> IngredientName.fromRawName(null));
    }

    @Test
    void rejectsBlankNameTest() {
        assertThrows(IllegalArgumentException.class, () -> IngredientName.fromRawName(""));
        assertThrows(IllegalArgumentException.class, () -> IngredientName.fromRawName(" \t\n "));
        assertThrows(IllegalArgumentException.class, () -> IngredientName.fromRawName(" _ _ "));
    }

    @Test
    void normalizationDoesNotDependOnDefaultLocaleTest() {
        Locale previousLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr"));
            IngredientName ingredient = IngredientName.fromRawName("ICEBERG LETTUCE");

            assertEquals("iceberg lettuce", ingredient.getKey());
            assertEquals(ingredient, IngredientName.fromRawName("iceberg lettuce"));
        }
        finally {
            Locale.setDefault(previousLocale);
        }
    }
}
