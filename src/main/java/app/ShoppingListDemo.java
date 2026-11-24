package app;

import data_access.FileMealPlanDataAccessObject;
import data_access.FilePantryDataAccessObject;
import data_access.FileShoppingListDataAccessObject;
import entity.IngredientName;
import entity.MealPlan;
import entity.MealSlot;
import entity.MealType;
import entity.Pantry;
import entity.PantryItem;
import entity.PlannedMeal;
import entity.RecipeIngredient;
import entity.StockLevel;
import interface_adapter.generate_shopping_list.GenerateShoppingListController;
import interface_adapter.generate_shopping_list.GenerateShoppingListPresenter;
import interface_adapter.load_shopping_list.LoadShoppingListController;
import interface_adapter.load_shopping_list.LoadShoppingListPresenter;
import interface_adapter.shopping_list.ShoppingListViewModel;
import interface_adapter.toggle_bought.ToggleBoughtController;
import interface_adapter.toggle_bought.ToggleBoughtPresenter;
import use_case.generate_shopping_list.DefaultShoppingNeedPolicy;
import use_case.generate_shopping_list.GenerateShoppingListInteractor;
import use_case.load_shopping_list.LoadShoppingListInteractor;
import use_case.toggle_bought.ToggleBoughtInteractor;
import view.ShoppingListView;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * Standalone demo for the shopping list use case.
 * Reads the meal plan from {@code data/mealplans.json} and the fridge from
 * {@code data/pantry.json}. On the first run, when those files have nothing in them,
 * it saves a sample plan and a sample fridge so there is something to show.
 * Tick items, close the window, run again, and the ticks are still there.
 */
public class ShoppingListDemo {

    /**
     * Starts the shopping list demo.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ShoppingListDemo::createAndShow);
    }

    private static void createAndShow() {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        FileMealPlanDataAccessObject mealPlanDao = new FileMealPlanDataAccessObject();
        if (mealPlanDao.getMealPlan(weekStart) == null) {
            mealPlanDao.saveMealPlan(sampleMealPlan(weekStart));
        }

        FilePantryDataAccessObject pantryDao = new FilePantryDataAccessObject();
        if (pantryDao.getPantry().sortedByExpiry().isEmpty()) {
            pantryDao.savePantry(samplePantry(today));
        }

        FileShoppingListDataAccessObject shoppingDao = new FileShoppingListDataAccessObject();

        ShoppingListViewModel viewModel = new ShoppingListViewModel();

        LoadShoppingListController loadController = new LoadShoppingListController(
                new LoadShoppingListInteractor(
                        shoppingDao, new LoadShoppingListPresenter(viewModel)));

        GenerateShoppingListController generateController = new GenerateShoppingListController(
                new GenerateShoppingListInteractor(
                        mealPlanDao,
                        pantryDao,
                        shoppingDao,
                        new DefaultShoppingNeedPolicy(),
                        new GenerateShoppingListPresenter(viewModel),
                        Clock.systemDefaultZone()));

        ToggleBoughtController toggleController = new ToggleBoughtController(
                new ToggleBoughtInteractor(
                        shoppingDao, new ToggleBoughtPresenter(viewModel)));

        ShoppingListView view = new ShoppingListView(
                viewModel, loadController, generateController, toggleController);

        JFrame frame = new JFrame("MealMate — Shopping List Demo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(view);
        frame.setSize(560, 480);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        view.onShown();
    }

    private static MealPlan sampleMealPlan(LocalDate weekStart) {
        MealPlan plan = new MealPlan(weekStart);
        plan.assign(new MealSlot(DayOfWeek.MONDAY, MealType.DINNER),
                new PlannedMeal("1", "Pasta", List.of(
                        new RecipeIngredient(IngredientName.fromRawName("Garlic"), "2 cloves"),
                        new RecipeIngredient(IngredientName.fromRawName("Olive Oil"), "1/4 cup"),
                        new RecipeIngredient(IngredientName.fromRawName("Tomato"), "2"),
                        new RecipeIngredient(IngredientName.fromRawName("Onion"), "1"))));
        plan.assign(new MealSlot(DayOfWeek.WEDNESDAY, MealType.LUNCH),
                new PlannedMeal("2", "Soup", List.of(
                        new RecipeIngredient(IngredientName.fromRawName("Garlic"), "1 clove"),
                        new RecipeIngredient(IngredientName.fromRawName("Carrot"), "2"),
                        new RecipeIngredient(IngredientName.fromRawName("Onion"), "1"))));
        plan.assign(new MealSlot(DayOfWeek.FRIDAY, MealType.DINNER),
                new PlannedMeal("3", "Stir Fry", List.of(
                        new RecipeIngredient(IngredientName.fromRawName("Chicken Breast"), "200g"),
                        new RecipeIngredient(IngredientName.fromRawName("Garlic"), "1 clove"),
                        new RecipeIngredient(IngredientName.fromRawName("Soy Sauce"), "2 tbsp"))));
        return plan;
    }

    private static Pantry samplePantry(LocalDate today) {
        Pantry pantry = new Pantry();
        pantry.add(new PantryItem(
                IngredientName.fromRawName("Olive Oil"), today.plusDays(30), StockLevel.ENOUGH));
        pantry.add(new PantryItem(
                IngredientName.fromRawName("Garlic"), today.minusDays(2), StockLevel.ENOUGH));
        pantry.add(new PantryItem(
                IngredientName.fromRawName("Onion"), today.plusDays(5), StockLevel.LOW));
        pantry.add(new PantryItem(
                IngredientName.fromRawName("Soy Sauce"), today.plusDays(60), StockLevel.ENOUGH));
        return pantry;
    }
}
