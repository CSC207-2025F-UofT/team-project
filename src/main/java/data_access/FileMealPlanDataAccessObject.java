package data_access;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import entity.IngredientName;
import entity.MealPlan;
import entity.MealSlot;
import entity.MealType;
import entity.PlannedMeal;
import entity.RecipeIngredient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.generate_shopping_list.MealPlanReader;
import use_case.load_meal_plan.LoadMealPlanDataAccessInterface;
import use_case.move_planned_meal.MovePlannedMealDataAccessInterface;
import use_case.remove_planned_meal.RemovePlannedMealDataAccessInterface;

/**
 * Persists every week's meal plan in one JSON file.
 *
 * <p>The file holds a {@code weeks} object whose keys are the Monday of each
 * week (ISO date, e.g. {@code 2026-11-23}). Each value is the list of meals
 * planned for that week:</p>
 * <pre>
 * {"weeks": {"2026-11-23": [
 *   {"day": "MONDAY", "mealType": "DINNER", "recipeId": "52771",
 *    "recipeName": "Spicy Arrabiata Penne", "cooked": false,
 *    "ingredients": [{"name": "garlic", "measure": "3 cloves"}]}
 * ]}}
 * </pre>
 */
public class FileMealPlanDataAccessObject
        implements LoadMealPlanDataAccessInterface,
        RemovePlannedMealDataAccessInterface,
        MovePlannedMealDataAccessInterface,
        MealPlanReader {
    private static final String KEY_WEEKS = "weeks";
    private static final String KEY_DAY = "day";
    private static final String KEY_MEAL_TYPE = "mealType";
    private static final String KEY_RECIPE_ID = "recipeId";
    private static final String KEY_RECIPE_NAME = "recipeName";
    private static final String KEY_COOKED = "cooked";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_NAME = "name";
    private static final String KEY_MEASURE = "measure";
    private static final int JSON_INDENT = 2;

    private final Path filePath;

    /**
     * Creates a DAO that stores meal plans at the given path. The file and its
     * parent folders are created if they do not exist yet.
     *
     * @param filePath the JSON file path
     */
    public FileMealPlanDataAccessObject(Path filePath) {
        this.filePath =
                Objects.requireNonNull(filePath, "file path cannot be null");
        ensureFileExists();
    }

    /**
     * Creates a DAO that stores meal plans in {@code data/mealplans.json}.
     */
    public FileMealPlanDataAccessObject() {
        this(Path.of("data", "mealplans.json"));
    }

    /**
     * Returns the saved meal plan for the given week, or null if that week
     * has no plan.
     *
     * @param weekStart the Monday that starts the week
     * @return a new MealPlan built from the file, or null
     * @throws IllegalStateException if the file cannot be read or parsed
     */
    @Override
    public MealPlan getMealPlan(LocalDate weekStart) {
        Objects.requireNonNull(weekStart, "week start cannot be null");
        final JSONObject weeks = readWeeks();
        final String weekKey = weekStart.toString();
        if (!weeks.has(weekKey)) {
            return null;
        }
        try {
            return fromJson(weekStart, weeks.getJSONArray(weekKey));
        }
        catch (JSONException | IllegalArgumentException
                | IllegalStateException exception) {
            throw new IllegalStateException(
                    "Could not parse meal plan file", exception);
        }
    }

    /**
     * Saves a meal plan, replacing the plan saved for the same week.
     * Plans of other weeks are kept unchanged.
     *
     * @param mealPlan the plan to save
     * @throws IllegalStateException if the file cannot be read or written
     */
    @Override
    public void saveMealPlan(MealPlan mealPlan) {
        Objects.requireNonNull(mealPlan, "meal plan cannot be null");
        final JSONObject weeks = readWeeks();
        weeks.put(mealPlan.getWeekStart().toString(), toJson(mealPlan));
        final JSONObject root = new JSONObject();
        root.put(KEY_WEEKS, weeks);
        writeRoot(root);
    }

    private void ensureFileExists() {
        try {
            final Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(filePath)) {
                final JSONObject root = new JSONObject();
                root.put(KEY_WEEKS, new JSONObject());
                writeRoot(root);
            }
        }
        catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not create meal plan file", exception);
        }
    }

    private JSONObject readWeeks() {
        try {
            final String content =
                    Files.readString(filePath, StandardCharsets.UTF_8);
            if (content.isBlank()) {
                return new JSONObject();
            }
            final JSONObject root = new JSONObject(content);
            if (!root.has(KEY_WEEKS)) {
                return new JSONObject();
            }
            return root.getJSONObject(KEY_WEEKS);
        }
        catch (IOException | JSONException exception) {
            throw new IllegalStateException(
                    "Could not read meal plan file", exception);
        }
    }

    private void writeRoot(JSONObject root) {
        try {
            Files.writeString(filePath, root.toString(JSON_INDENT),
                    StandardCharsets.UTF_8);
        }
        catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not write meal plan file", exception);
        }
    }

    private MealPlan fromJson(LocalDate weekStart, JSONArray mealsJson) {
        final MealPlan plan = new MealPlan(weekStart);
        for (int index = 0; index < mealsJson.length(); index++) {
            final JSONObject mealJson = mealsJson.getJSONObject(index);
            final MealSlot slot = new MealSlot(
                    DayOfWeek.valueOf(mealJson.getString(KEY_DAY)),
                    MealType.valueOf(mealJson.getString(KEY_MEAL_TYPE)));
            final List<RecipeIngredient> ingredients =
                    ingredientsFromJson(mealJson.getJSONArray(KEY_INGREDIENTS));
            final PlannedMeal meal = new PlannedMeal(
                    mealJson.getString(KEY_RECIPE_ID),
                    mealJson.getString(KEY_RECIPE_NAME),
                    ingredients);
            if (mealJson.getBoolean(KEY_COOKED)) {
                meal.markCooked();
            }
            plan.assign(slot, meal);
        }
        return plan;
    }

    private List<RecipeIngredient> ingredientsFromJson(
            JSONArray ingredientsJson) {
        final List<RecipeIngredient> ingredients = new ArrayList<>();
        for (int index = 0; index < ingredientsJson.length(); index++) {
            final JSONObject ingredientJson =
                    ingredientsJson.getJSONObject(index);
            final IngredientName name = IngredientName.fromRawName(
                    ingredientJson.getString(KEY_NAME));
            final String measure;
            if (ingredientJson.isNull(KEY_MEASURE)) {
                measure = null;
            }
            else {
                measure = ingredientJson.getString(KEY_MEASURE);
            }
            ingredients.add(new RecipeIngredient(name, measure));
        }
        return ingredients;
    }

    private JSONArray toJson(MealPlan mealPlan) {
        final JSONArray mealsJson = new JSONArray();
        for (Map.Entry<MealSlot, PlannedMeal> entry
                : mealPlan.getMeals().entrySet()) {
            final MealSlot slot = entry.getKey();
            final PlannedMeal meal = entry.getValue();
            final JSONObject mealJson = new JSONObject();
            mealJson.put(KEY_DAY, slot.getDay().name());
            mealJson.put(KEY_MEAL_TYPE, slot.getMealType().name());
            mealJson.put(KEY_RECIPE_ID, meal.getRecipeId());
            mealJson.put(KEY_RECIPE_NAME, meal.getRecipeName());
            mealJson.put(KEY_COOKED, meal.isCooked());
            mealJson.put(KEY_INGREDIENTS,
                    ingredientsToJson(meal.getIngredients()));
            mealsJson.put(mealJson);
        }
        return mealsJson;
    }

    private JSONArray ingredientsToJson(List<RecipeIngredient> ingredients) {
        final JSONArray ingredientsJson = new JSONArray();
        for (RecipeIngredient ingredient : ingredients) {
            final JSONObject ingredientJson = new JSONObject();
            ingredientJson.put(KEY_NAME, ingredient.getDisplayName());
            final String measure = ingredient.getMeasureDescription();
            ingredientJson.put(KEY_MEASURE,
                    measure == null ? JSONObject.NULL : measure);
            ingredientsJson.put(ingredientJson);
        }
        return ingredientsJson;
    }
}
