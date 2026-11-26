package org.example.cocktailcraft.data_access;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.RecipeIngredient;

/** Translations for the bundled catalog, keyed by the exact source text. */
public final class RecipeTranslations {
    private final Map<String, Map<String, String>> entries;

    public RecipeTranslations(String language) {
        String path = "/i18n/recipes_" + language + ".json";
        InputStream input = RecipeTranslations.class.getResourceAsStream(path);
        if (input == null) {
            entries = Map.of();
        } else {
            try (Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                entries = new Gson().fromJson(reader, new TypeToken<Map<String, Map<String, String>>>() { }
                        .getType());
            } catch (IOException exception) {
                throw new UncheckedIOException(exception);
            }
        }
    }

    public Cocktail translate(Cocktail cocktail) {
        if (entries.isEmpty()) {
            return cocktail;
        }
        List<RecipeIngredient> ingredients = cocktail.getIngredients().stream()
                .map(item -> new RecipeIngredient(lookup("ingredients", item.getName()), item.getAmount(),
                        lookup("units", item.getUnit())))
                .collect(Collectors.toList());
        return new Cocktail(cocktail.getId(), lookup("names", cocktail.getName()), ingredients,
                cocktail.getTags(), lookup("instructions", cocktail.getInstructions()), cocktail.getAbv());
    }

    private String lookup(String field, String original) {
        return entries.getOrDefault(field, Map.of()).getOrDefault(original, original);
    }
}
