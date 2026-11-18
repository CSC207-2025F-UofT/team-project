package org.example.cocktailcraft.data_access;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;
import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.RecipeIngredient;
import org.example.cocktailcraft.entity.Tags;
import org.example.cocktailcraft.entity.Tags.BaseSpirit;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.EnumSet;
import java.util.Set;

public class CocktailJsonReader {
    public static final String DATASET_INDEX = "/bar-assistant/cocktail-index.txt";
    private final Map<String, String> ingredientParents = new HashMap<>();

    /** Reads the bundled dataset once per call; callers should retain the result. */
    public List<Cocktail> readAll() throws IOException {
        return readAll(DATASET_INDEX);
    }

    public List<Cocktail> readAll(String indexPath) throws IOException {
        List<Cocktail> cocktails = new ArrayList<>();
        Set<String> seenPaths = new HashSet<>();
        try (BufferedReader index = new BufferedReader(openResource(indexPath))) {
            String line;
            int lineNumber = 0;
            while ((line = index.readLine()) != null) {
                lineNumber++;
                String path = line.trim();
                if (path.isEmpty()) {
                    continue;
                }
                if (!path.startsWith("/") || !seenPaths.add(path)) {
                    throw new IOException("Invalid or duplicate resource path in "
                            + indexPath + " at line " + lineNumber + ": " + path);
                }
                cocktails.add(read(path));
            }
        }
        if (cocktails.isEmpty()) {
            throw new IOException("Dataset index is empty: " + indexPath);
        }
        return List.copyOf(cocktails);
    }

    public Cocktail read(String resourcePath) throws IOException {
        try (Reader reader = openResource(resourcePath)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            String id = json.get("_id").getAsString();
            String name = json.get("name").getAsString();
            List<RecipeIngredient> ingredients = new ArrayList<>();
            Set<BaseSpirit> baseSpirits = EnumSet.noneOf(BaseSpirit.class);

            for (JsonElement element : json.getAsJsonArray("ingredients")) {
                JsonObject ingredient = element.getAsJsonObject();
                baseSpirits.addAll(readBaseSpirits(ingredient.get("_id").getAsString()));

                ingredients.add(new RecipeIngredient(
                        ingredient.get("name").getAsString(),
                        ingredient.get("amount").getAsString(),
                        ingredient.get("units").getAsString()
                ));
            }

            return new Cocktail(id, name, ingredients, new Tags(baseSpirits));
        } catch (JsonParseException | IllegalStateException | NullPointerException
                 | UnsupportedOperationException exception) {
            throw new IOException("Invalid cocktail JSON: " + resourcePath, exception);
        }
    }

    private Set<BaseSpirit> readBaseSpirits(String ingredientId) throws IOException {
        Set<String> visited = new HashSet<>();
        String currentId = ingredientId;
        while (currentId != null) {
            if (!visited.add(currentId)) {
                throw new IOException("Ingredient parent cycle: " + ingredientId);
            }
            switch (currentId) {
                case "gin":
                    return Set.of(BaseSpirit.GIN);
                case "vodka":
                    return Set.of(BaseSpirit.VODKA);
                case "rum":
                    return Set.of(BaseSpirit.RUM);
                case "tequila":
                    return Set.of(BaseSpirit.TEQUILA);
                case "whiskey":
                    return Set.of(BaseSpirit.WHISKEY);
                case "brandy":
                    return Set.of(BaseSpirit.BRANDY);
                case "spirits":
                    return Set.of(BaseSpirit.OTHER);
                default:
                    currentId = readParentId(currentId);
            }
        }
        return Set.of();
    }

    private String readParentId(String ingredientId) throws IOException {
        if (!ingredientParents.containsKey(ingredientId)) {
            String path = "/bar-assistant/data/ingredients/" + ingredientId + "/data.json";
            try (Reader reader = openResource(path)) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                JsonElement parent = json.get("_parent_id");
                ingredientParents.put(ingredientId, parent.isJsonNull() ? null : parent.getAsString());
            } catch (JsonParseException | IllegalStateException | NullPointerException
                     | UnsupportedOperationException exception) {
                throw new IOException("Invalid ingredient JSON: " + path, exception);
            }
        }
        return ingredientParents.get(ingredientId);
    }

    private Reader openResource(String resourcePath) throws IOException {
        if (resourcePath == null || !resourcePath.startsWith("/")) {
            throw new IOException("Expected an absolute resource path: " + resourcePath);
        }
        InputStream stream = CocktailJsonReader.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        return new InputStreamReader(stream, StandardCharsets.UTF_8);
    }
}
