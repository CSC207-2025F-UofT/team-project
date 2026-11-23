package use_case.load_meal_plan;

import entity.MealPlan;
import use_case.meal_plan.MealPlanWeekOutputData;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * Loads the meal plan of the week containing the requested day (use case 3a).
 * If nothing is saved for that week, presents an empty plan instead.
 */
public class LoadMealPlanInteractor implements LoadMealPlanInputBoundary {

    private final LoadMealPlanDataAccessInterface dataAccess;
    private final LoadMealPlanOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param dataAccess the meal plan data access
     * @param presenter the output boundary
     */
    public LoadMealPlanInteractor(LoadMealPlanDataAccessInterface dataAccess,
                                  LoadMealPlanOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoadMealPlanInputData inputData) {
        LocalDate weekStart = inputData.getDate()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        MealPlan saved = dataAccess.getMealPlan(weekStart);
        MealPlan toShow = saved == null ? new MealPlan(weekStart) : saved;
        presenter.prepareSuccessView(
                new LoadMealPlanOutputData(MealPlanWeekOutputData.from(toShow)));
    }
}
