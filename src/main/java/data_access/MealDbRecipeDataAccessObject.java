package data_access;
import entity.Ingredient;
import entity.Recipe;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.recipe.RecipeDataAccessInterface;

import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.time.Duration;
import java.util.*;

public class MealDbRecipeDataAccessObject implements RecipeDataAccessInterface {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private final HttpClient client;
    public MealDbRecipeDataAccessObject(){
        this.client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    }

    @Override
    public List<Recipe> searchByName(String keyword){
        //TODO: Use case 1
    }

    @Override
    public List<Recipe> filterByCategory(String category){
        if (category == null || category.trim().isEmpty()){
            return new ArrayList<>();
        }
        String chosenCategory = category.trim();
        String path = "filter.php?c=" + encode(chosenCategory);
        return parseRecipes(request(path), chosenCategory);
    }

    @Override
    public List<String> getCategories(){
        JSONArray data = request("list.php?c=list").optJSONArray("meals");
        List<String> categories = new ArrayList<>();
        if (data == null){
            return categories;
        }
        for (int i = 0; i < data.length(); i++){
            String category = data.getJSONObject(i).optString("strCategory", "").trim();
            if (!category.isEmpty()){
                categories.add(category);
            }
        }
        return categories;
    }

    @Override
    public Recipe getRecipeByID(String id){
        if (id == null || id.trim().isEmpty()){
            return null;
        }
        List<Recipe> matches = parseRecipes(request("lookup.php?i=" + encode(id.trim())), null);
        if (matches.isEmpty()){
            return null;
        }
        return matches.get(0);
    }

    //helper methods
    private String encode(String value){
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
    private List<Recipe> parseRecipes(JSONObject result, String categoryFromQuery){
        List<Recipe> recipes = new ArrayList<>();
        JSONArray meals = result.optJSONArray("meals");
        if (meals == null){
            return recipes;
        }
        for (int i = 0; i < meals.length(); i++){
            recipes.add(parseRecipe(meals.getJSONObject(i), categoryFromQuery));
        }
        return recipes;
    }


    private Recipe parseRecipe(JSONObject meal, String categoryFromQuery){
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 1; i <= 20; i++){
            String ingredient = meal.optString("strIngredient" + i, "").trim();
            String measurement = meal.optString("strMeasure" + i, "").trim();
            if (!ingredient.isEmpty()){
                ingredients.add(new Ingredient(ingredient, measurement));
            }
        }
        String category = meal.optString("strCategory", "");
        if (category.isEmpty() && categoryFromQuery != null) {
            category = categoryFromQuery;
        }
        return new Recipe(meal.optString("idMeal", ""),
                meal.optString("strMeal", ""),
                category,
                meal.optString("strArea", ""),
                meal.optString("strInstructions", ""),
                meal.optString("strMealThumb", ""),
                ingredients);
    }

    private JSONObject request(String path){
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + path))
                .timeout(Duration.ofSeconds(15)).GET().build();
        try {
            HttpResponse<String> response = client.send(
                    request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalStateException(
                        "TheMealDB returned HTTP " + response.statusCode());
            }
            return new JSONObject(response.body());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("TheMealDB request was interrupted", ex);
        } catch (IOException ex) {
            throw new IllegalStateException("TheMealDB request failed", ex);
        }
    }
}
