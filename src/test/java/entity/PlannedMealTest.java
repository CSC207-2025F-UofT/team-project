package entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlannedMealTest {

    @Test
    void copiesIngredientsAndMarksCookedOnce() {
        RecipeIngredient garlic =
                new RecipeIngredient(IngredientName.fromRawName("Garlic"), "1 clove");
        PlannedMeal meal = new PlannedMeal("1", "Pasta", List.of(garlic));

        assertEquals("1", meal.getRecipeId());
        assertEquals("Pasta", meal.getRecipeName());
        assertEquals(1, meal.getIngredients().size());
        assertEquals("Garlic", meal.getIngredients().get(0).getName().getDisplayName());
        assertFalse(meal.isCooked());

        meal.markCooked();
        assertTrue(meal.isCooked());
        assertThrows(IllegalStateException.class, meal::markCooked);
        assertThrows(UnsupportedOperationException.class,
                () -> meal.getIngredients().add(garlic));
    }

    @Test
    void rejectsNullConstructionArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new PlannedMeal(null, "Pasta", List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> new PlannedMeal("1", null, List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> new PlannedMeal("1", "Pasta", null));
    }
}
