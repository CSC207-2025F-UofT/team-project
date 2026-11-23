package app;

import data_access.InMemoryMealPlanDataAccessObject;
import entity.IngredientName;
import entity.MealPlan;
import entity.MealSlot;
import entity.MealType;
import entity.PlannedMeal;
import entity.RecipeIngredient;
import interface_adapter.load_meal_plan.LoadMealPlanController;
import interface_adapter.load_meal_plan.LoadMealPlanPresenter;
import interface_adapter.meal_plan.MealPlanViewModel;
import interface_adapter.move_planned_meal.MovePlannedMealController;
import interface_adapter.move_planned_meal.MovePlannedMealPresenter;
import interface_adapter.remove_planned_meal.RemovePlannedMealController;
import interface_adapter.remove_planned_meal.RemovePlannedMealPresenter;
import use_case.load_meal_plan.LoadMealPlanInteractor;
import use_case.move_planned_meal.MovePlannedMealInteractor;
import use_case.remove_planned_meal.RemovePlannedMealInteractor;
import view.MealPlanView;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * Runs the meal plan page on its own with sample data, until the main app
 * (ViewManager, nav bar, AppBuilder) is ready.
 */
public class MealPlanDemo {

    /**
     * Opens the demo window.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MealPlanDemo::createAndShow);
    }

    private static void createAndShow() {
        LocalDate weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        InMemoryMealPlanDataAccessObject mealPlanDao = new InMemoryMealPlanDataAccessObject();
        mealPlanDao.saveMealPlan(sampleMealPlan(weekStart));

        MealPlanView view = buildView(mealPlanDao);

        JFrame frame = new JFrame("MealMate - Meal Plan Demo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(view);
        frame.setSize(1000, 460);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        view.onShown();
    }

    /**
     * Wires the meal plan page to the given data access object.
     *
     * @param mealPlanDao where meal plans are read from and saved to
     * @return the meal plan page
     */
    public static MealPlanView buildView(InMemoryMealPlanDataAccessObject mealPlanDao) {
        MealPlanViewModel viewModel = new MealPlanViewModel();
        LoadMealPlanController loadController = new LoadMealPlanController(
                new LoadMealPlanInteractor(mealPlanDao, new LoadMealPlanPresenter(viewModel)));
        RemovePlannedMealController removeController = new RemovePlannedMealController(
                new RemovePlannedMealInteractor(mealPlanDao, new RemovePlannedMealPresenter(viewModel)));
        MovePlannedMealController moveController = new MovePlannedMealController(
                new MovePlannedMealInteractor(mealPlanDao, new MovePlannedMealPresenter(viewModel)));
        return new MealPlanView(viewModel, loadController, removeController, moveController);
    }

    private static MealPlan sampleMealPlan(LocalDate weekStart) {
        MealPlan plan = new MealPlan(weekStart);
        plan.assign(new MealSlot(DayOfWeek.MONDAY, MealType.DINNER),
                new PlannedMeal("52771", "Spicy Arrabiata Penne", List.of(
                        ingredient("penne rigate", "1 pound"),
                        ingredient("olive oil", "1/4 cup"),
                        ingredient("garlic", "3 cloves"),
                        ingredient("Parmigiano-Reggiano", "sprinkling"))));
        plan.assign(new MealSlot(DayOfWeek.WEDNESDAY, MealType.LUNCH),
                new PlannedMeal("2", "Carrot Soup", List.of(
                        ingredient("Carrots", "4"),
                        ingredient("Onion", "1"),
                        ingredient("garlic", "1 clove"))));
        PlannedMeal cooked = new PlannedMeal("3", "Pancakes", List.of(
                ingredient("Flour", "1 cup"),
                ingredient("Eggs", "2"),
                ingredient("Milk", "1 cup")));
        cooked.markCooked();
        plan.assign(new MealSlot(DayOfWeek.TUESDAY, MealType.BREAKFAST), cooked);
        plan.assign(new MealSlot(DayOfWeek.FRIDAY, MealType.DINNER),
                new PlannedMeal("4", "Chicken Stir Fry", List.of(
                        ingredient("Chicken Breast", "200g"),
                        ingredient("Soy Sauce", "2 tbsp"))));
        return plan;
    }

    private static RecipeIngredient ingredient(String name, String measure) {
        return new RecipeIngredient(IngredientName.fromRawName(name), measure);
    }
}
