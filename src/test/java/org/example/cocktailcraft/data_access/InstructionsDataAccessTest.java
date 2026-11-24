package org.example.cocktailcraft.data_access;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.CustomRecipe;
import org.example.cocktailcraft.entity.RecipeIngredient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InstructionsDataAccessTest {
    @TempDir
    Path directory;

    @Test
    void readsOriginalInstructionsFromBundledDataset() throws IOException {
        Cocktail cocktail = new CocktailJsonReader().read(
                "/bar-assistant/data/cocktails/8-amaro-sazerac/data.json");
        assertEquals("Stir with ice, strain into a rocks glass pre-rinsed with Green Chartreuse, "
                + "and garnish with a lemon twist.", cocktail.getInstructions());
    }

    @Test
    void handlesMissingAndNullInstructionsInSourceData() throws IOException {
        CocktailJsonReader reader = new CocktailJsonReader();
        assertEquals("", reader.read("/fixtures/no-instructions.json").getInstructions());
        assertEquals("", reader.read("/fixtures/null-instructions.json").getInstructions());
    }

    @Test
    void reloadsAndUpdatesMultilineInstructionsWithoutDuplicates() throws IOException {
        Path file = directory.resolve("custom-recipes.json");
        FileCustomRecipeDataAccessObject first = new FileCustomRecipeDataAccessObject(file);
        String initial = "1. Add 砂糖 and \"lime\".\n2. Stir.\n\nServe chilled.";
        first.save(recipe(initial));

        FileCustomRecipeDataAccessObject restarted = new FileCustomRecipeDataAccessObject(file);
        assertEquals(initial, restarted.findById("custom-001").orElseThrow().getInstructions());
        String edited = "1. Use less sugar.\n2. Shake instead of stirring.";
        restarted.save(recipe(edited));

        FileCustomRecipeDataAccessObject reloaded = new FileCustomRecipeDataAccessObject(file);
        assertEquals(edited, reloaded.findById("custom-001").orElseThrow().getInstructions());
        assertEquals(1, com.google.gson.JsonParser.parseString(Files.readString(file))
                .getAsJsonArray().size());
    }

    @Test
    void loadsLegacyRecipesAndCanAddInstructions() throws IOException {
        Path file = directory.resolve("legacy.json");
        String legacy = "[{\"id\":\"custom-001\",\"ownerId\":\"user-001\","
                + "\"originalCocktailId\":\"mojito\",\"name\":\"My Mojito\","
                + "\"ingredients\":[{\"ingredient\":\"Sugar\",\"amount\":\"1\",\"unit\":\"tsp\"}]}]";
        Files.writeString(file, legacy);
        FileCustomRecipeDataAccessObject dao = new FileCustomRecipeDataAccessObject(file);
        CustomRecipe old = dao.findById("custom-001").orElseThrow();
        assertEquals("", old.getInstructions());
        assertEquals("Sugar", old.getIngredients().get(0).getName());
        dao.save(recipe("Stir with ice."));
        assertEquals("Stir with ice.", new FileCustomRecipeDataAccessObject(file)
                .findById("custom-001").orElseThrow().getInstructions());
        assertTrue(Files.readString(file).contains("instructions"));
    }

    private CustomRecipe recipe(String instructions) {
        return new CustomRecipe("custom-001", "user-001", "mojito", "My Mojito",
                List.of(new RecipeIngredient("Sugar", "1", "tsp")), instructions);
    }
}
