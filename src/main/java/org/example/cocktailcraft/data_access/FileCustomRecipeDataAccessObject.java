package org.example.cocktailcraft.data_access;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import org.example.cocktailcraft.entity.CustomRecipe;
import org.example.cocktailcraft.use_case.customize.CustomizeDataAccessInterface;

public class FileCustomRecipeDataAccessObject
        implements CustomizeDataAccessInterface {

    private final Path filePath;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public FileCustomRecipeDataAccessObject(Path filePath) {
        this.filePath = filePath.toAbsolutePath();
    }

    @Override
    public Optional<CustomRecipe> findById(String customRecipeId)
            throws IOException {

        for (CustomRecipe recipe : readRecipes()) {
            if (recipe.getId().equals(customRecipeId)) {
                return Optional.of(recipe);
            }
        }

        return Optional.empty();
    }

    @Override
    public void save(CustomRecipe recipe) throws IOException {
        List<CustomRecipe> recipes = readRecipes();

        recipes.removeIf(existing ->
                existing.getId().equals(recipe.getId()));

        recipes.add(recipe);

        writeRecipes(recipes);
    }

    private List<CustomRecipe> readRecipes() throws IOException {
        List<CustomRecipe> recipes = new ArrayList<>();

        if (Files.notExists(filePath)) {
            return recipes;
        }

        try (Reader reader =
                     Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {

            CustomRecipe[] savedRecipes =
                    gson.fromJson(reader, CustomRecipe[].class);

            if (savedRecipes == null) {
                throw new IOException("Custom recipe file is empty or invalid.");
            }

            for (CustomRecipe recipe : savedRecipes) {
                if (recipe == null
                        || recipe.getId() == null
                        || recipe.getOwnerId() == null
                        || recipe.getOriginalCocktailId() == null
                        || recipe.getName() == null
                        || recipe.getIngredients() == null) {

                    throw new IOException("Invalid custom recipe data.");
                }

                recipes.add(new CustomRecipe(
                        recipe.getId(),
                        recipe.getOwnerId(),
                        recipe.getOriginalCocktailId(),
                        recipe.getName(),
                        recipe.getIngredients(),
                        recipe.getInstructions()
                ));
            }

            return recipes;
        } catch (JsonParseException | NullPointerException exception) {
            throw new IOException(
                    "Unable to read custom recipe data.",
                    exception
            );
        }
    }

    private void writeRecipes(List<CustomRecipe> recipes) throws IOException {
        Path directory = filePath.getParent();
        Files.createDirectories(directory);

        Path temporaryFile =
                Files.createTempFile(directory, "custom-recipes-", ".tmp");

        try {
            Files.writeString(
                    temporaryFile,
                    gson.toJson(recipes),
                    StandardCharsets.UTF_8
            );

            try {
                Files.move(
                        temporaryFile,
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE
                );
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(
                        temporaryFile,
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }
}
