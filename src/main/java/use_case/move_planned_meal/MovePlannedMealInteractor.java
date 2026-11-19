package use_case.move_planned_meal;

import entity.MealPlan;
import entity.MealSlot;
import entity.PlannedMeal;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * Moves a planned meal to an empty slot of the same week (use case 3b, "Move").
 * A cooked meal cannot be moved, and the target slot must be empty.
 */
public class MovePlannedMealInteractor implements MovePlannedMealInputBoundary {

    static final String NO_MEAL = "There is no meal in that slot.";
    static final String SAME_SLOT = "Pick a different slot to move the meal to.";
    static final String ALREADY_COOKED = "This meal has already been cooked, so it can't be moved.";
    static final String SLOT_TAKEN = "That slot already has a meal. Pick an empty slot.";

    private final MovePlannedMealDataAccessInterface dataAccess;
    private final MovePlannedMealOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param dataAccess the meal plan data access
     * @param presenter the output boundary
     */
    public MovePlannedMealInteractor(MovePlannedMealDataAccessInterface dataAccess,
                                     MovePlannedMealOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(MovePlannedMealInputData inputData) {
        LocalDate weekStart = inputData.getWeekStart()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        MealSlot from = new MealSlot(inputData.getFromDay(), inputData.getFromMealType());
        MealSlot to = new MealSlot(inputData.getToDay(), inputData.getToMealType());

        MealPlan plan = dataAccess.getMealPlan(weekStart);
        PlannedMeal meal = plan == null ? null : plan.getMeals().get(from);
        if (meal == null) {
            presenter.prepareFailView(NO_MEAL);
            return;
        }
        if (from.equals(to)) {
            presenter.prepareFailView(SAME_SLOT);
            return;
        }
        if (meal.isCooked()) {
            presenter.prepareFailView(ALREADY_COOKED);
            return;
        }
        if (plan.getMeals().containsKey(to)) {
            presenter.prepareFailView(SLOT_TAKEN);
            return;
        }

        plan.move(from, to);
        dataAccess.saveMealPlan(plan);
        presenter.prepareSuccessView(new MovePlannedMealOutputData(plan, meal.getRecipeName()));
    }
}
