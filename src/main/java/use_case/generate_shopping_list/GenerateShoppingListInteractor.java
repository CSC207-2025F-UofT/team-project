package use_case.generate_shopping_list;

import entity.IngredientName;
import entity.MealPlan;
import entity.Pantry;
import entity.PantryItem;
import entity.PlannedMeal;
import entity.RecipeIngredient;
import entity.ShoppingItem;
import entity.ShoppingList;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Builds a shopping list from this week's uncooked meals and the fridge.
 */
public class GenerateShoppingListInteractor implements GenerateShoppingListInputBoundary {

    private static final String NOTHING_PLANNED = "Nothing planned to shop for.";
    private static final String HAVE_EVERYTHING = "You have everything you need.";

    private final MealPlanReader mealPlanReader;
    private final PantryReader pantryReader;
    private final GenerateShoppingListDataAccessInterface shoppingListDataAccess;
    private final ShoppingNeedPolicy needPolicy;
    private final GenerateShoppingListOutputBoundary presenter;
    private final Clock clock;

    /**
     * Creates the interactor.
     *
     * @param mealPlanReader read-only meal plan access
     * @param pantryReader read-only pantry access
     * @param shoppingListDataAccess shopping list load/save
     * @param needPolicy strategy for MISSING / LOW / skip
     * @param presenter the output boundary
     * @param clock clock used to determine "today"
     */
    public GenerateShoppingListInteractor(MealPlanReader mealPlanReader,
                                          PantryReader pantryReader,
                                          GenerateShoppingListDataAccessInterface shoppingListDataAccess,
                                          ShoppingNeedPolicy needPolicy,
                                          GenerateShoppingListOutputBoundary presenter,
                                          Clock clock) {
        this.mealPlanReader = mealPlanReader;
        this.pantryReader = pantryReader;
        this.shoppingListDataAccess = shoppingListDataAccess;
        this.needPolicy = needPolicy;
        this.presenter = presenter;
        this.clock = clock;
    }

    @Override
    public void execute(GenerateShoppingListInputData inputData) {
        LocalDate today = LocalDate.now(clock);
        LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        MealPlan mealPlan = mealPlanReader.getMealPlan(weekStart);
        List<PlannedMeal> uncooked = mealPlan == null ? List.of() : mealPlan.uncookedMeals();
        if (uncooked.isEmpty()) {
            presenter.prepareFailView(new GenerateShoppingListOutputData(
                    null, NOTHING_PLANNED, true));
            return;
        }

        Map<IngredientName, Set<String>> neededRecipes = collectIngredients(uncooked);
        Pantry pantry = pantryReader.getPantry();
        ShoppingList previous = shoppingListDataAccess.loadShoppingList(weekStart);

        List<ShoppingItem> items = new ArrayList<>();
        for (Map.Entry<IngredientName, Set<String>> entry : neededRecipes.entrySet()) {
            IngredientName name = entry.getKey();
            PantryItem pantryItem = pantry.find(name).orElse(null);
            ShoppingItem.Reason reason = needPolicy.classify(pantryItem, today);
            if (reason == null) {
                continue;
            }
            ShoppingItem item = new ShoppingItem(name, reason, entry.getValue());
            if (previous != null) {
                ShoppingItem oldItem = previous.find(name);
                if (oldItem != null && oldItem.isBought()) {
                    item.setBought(true);
                }
            }
            items.add(item);
        }

        items.sort(Comparator
                .comparing(ShoppingItem::getReason)
                .thenComparing(item -> item.getName().getDisplayName(),
                        String.CASE_INSENSITIVE_ORDER));

        ShoppingList list = new ShoppingList(weekStart, items);
        shoppingListDataAccess.saveShoppingList(list);

        String message = items.isEmpty() ? HAVE_EVERYTHING : "";
        presenter.prepareSuccessView(new GenerateShoppingListOutputData(list, message, false));
    }

    private Map<IngredientName, Set<String>> collectIngredients(List<PlannedMeal> meals) {
        Map<IngredientName, Set<String>> result = new LinkedHashMap<>();
        for (PlannedMeal meal : meals) {
            for (RecipeIngredient ingredient : meal.getIngredients()) {
                IngredientName name = ingredient.getName();
                result.computeIfAbsent(name, key -> new LinkedHashSet<>())
                        .add(meal.getRecipeName());
            }
        }
        return result;
    }
}
