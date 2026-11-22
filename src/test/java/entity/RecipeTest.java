package entity;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecipeTest {

    @Test
    void retainsSnapshotAndProtectsItsIngredients() {
        RecipeIngredient garlic = new RecipeIngredient(IngredientName.fromRawName("Garlic"), "1 clove");
        List<RecipeIngredient> source = new ArrayList<>(List.of(garlic));
        URI thumbnail = URI.create("https://example.com/recipe.jpg");
        Recipe recipe = new Recipe("meal:52772", "Pasta", "Main", "Italian",
                "Cook pasta", thumbnail, source);

        source.clear();
        List<RecipeIngredient> returned = recipe.getIngredients();
        returned.clear();

        assertEquals("meal:52772", recipe.getId());
        assertEquals("Pasta", recipe.getName());
        assertEquals("Main", recipe.getCategory());
        assertEquals("Italian", recipe.getArea());
        assertEquals("Cook pasta", recipe.getInstruction());
        assertEquals(thumbnail, recipe.getThumbnail());
        assertEquals(List.of(garlic), recipe.getIngredients());
    }

    @Test
    void permitsUnavailableOptionalInformation() {
        Recipe recipe = new Recipe("local:123", "Soup", null, null, null, null, List.of());

        assertNull(recipe.getCategory());
        assertNull(recipe.getArea());
        assertNull(recipe.getInstruction());
        assertNull(recipe.getThumbnail());
        assertEquals(List.of(), recipe.getIngredients());
    }

    @Test
    void rejectsNullRequiredData() {
        assertThrows(NullPointerException.class,
                () -> new Recipe(null, "Soup", null, null, null, null, List.of()));
        assertThrows(NullPointerException.class,
                () -> new Recipe("local:123", null, null, null, null, null, List.of()));
        assertThrows(NullPointerException.class,
                () -> new Recipe("local:123", "Soup", null, null, null, null, null));
    }
}
