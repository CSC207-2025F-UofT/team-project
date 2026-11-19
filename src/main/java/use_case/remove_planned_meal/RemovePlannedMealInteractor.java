package use_case.remove_planned_meal;

import entity.MealPlan;
import entity.MealSlot;
import entity.PlannedMeal;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * Removes the meal in one slot of the week (use case 3b, "Remove").
 */
public class RemovePlannedMealInteractor implements RemovePlannedMealInputBoundary {

    static final String NO_MEAL = "There is no meal in that slot.";

    private final RemovePlannedMealDataAccessInterface dataAccess;
    private final RemovePlannedMealOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param dataAccess the meal plan data access
     * @param presenter the output boundary
     */
    public RemovePlannedMealInteractor(RemovePlannedMealDataAccessInterface dataAccess,
                                       RemovePlannedMealOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(RemovePlannedMealInputData inputData) {
        LocalDate weekStart = inputData.getWeekStart()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        MealSlot slot = new MealSlot(inputData.getDay(), inputData.getMealType());

        MealPlan plan = dataAccess.getMealPlan(weekStart);
        PlannedMeal meal = plan == null ? null : plan.getMeals().get(slot);
        if (meal == null) {
            presenter.prepareFailView(NO_MEAL);
            return;
        }

        plan.remove(slot);
        dataAccess.saveMealPlan(plan);
        presenter.prepareSuccessView(new RemovePlannedMealOutputData(plan, meal.getRecipeName()));
    }
}
