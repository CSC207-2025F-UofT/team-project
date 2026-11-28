package use_case.edit_planned_meal;

import entity.DailyMealPlan;
import entity.FoodPortion;
import entity.MealSchedule;
import entity.NutritionTotals;
import entity.PlannedMeal;

import use_case.meal_planning.AddPlannedMealDataAccessInterface;
import use_case.meal_planning.AddPlannedMealDataAccessException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChangePlannedPortionInteractor
        implements ChangePlannedPortionInputBoundary {

    private final AddPlannedMealDataAccessInterface dataAccess;
    private final ChangePlannedPortionOutputBoundary presenter;

    public ChangePlannedPortionInteractor(
            AddPlannedMealDataAccessInterface dataAccess,
            ChangePlannedPortionOutputBoundary presenter) {
        this.dataAccess = Objects.requireNonNull(dataAccess);
        this.presenter = Objects.requireNonNull(presenter);
    }

    @Override
    public void execute(ChangePlannedPortionInputData inputData) {
        if (inputData == null || inputData.getDate() == null) {
            presenter.prepareFailView("Please select a date.");
            return;
        }
        if (inputData.getMealSlotId() == null
                || inputData.getMealSlotId().isBlank()) {
            presenter.prepareFailView("Please select a meal slot.");
            return;
        }
        if (inputData.getPortionIndex() < 0) {
            presenter.prepareFailView("Please select a food portion.");
            return;
        }
        if (!Double.isFinite(inputData.getNewGrams())
                || inputData.getNewGrams() <= 0) {
            presenter.prepareFailView(
                    "Food quantity must be greater than zero.");
            return;
        }

        try {
            DailyMealPlan storedPlan =
                    dataAccess.findPlanByDate(inputData.getDate());
            if (storedPlan == null) {
                presenter.prepareFailView(
                        "No meal plan exists for this date.");
                return;
            }

            MealSchedule schedule =
                    dataAccess.findScheduleByPlanId(storedPlan.getId());
            if (schedule == null
                    || schedule.getSlotById(
                    inputData.getMealSlotId()) == null) {
                presenter.prepareFailView(
                        "The selected meal slot does not exist.");
                return;
            }

            DailyMealPlan updatedPlan = copyPlan(storedPlan);
            PlannedMeal meal = updatedPlan.getMealBySlotId(
                    inputData.getMealSlotId());
            if (meal == null) {
                presenter.prepareFailView(
                        "This meal slot has no planned meal.");
                return;
            }

            int index = inputData.getPortionIndex();
            if (index >= meal.getPortions().size()) {
                presenter.prepareFailView(
                        "The selected food portion does not exist.");
                return;
            }

            FoodPortion oldPortion = meal.getPortions().get(index);
            NutritionTotals totals;
            try {
                double factor = inputData.getNewGrams()
                        / oldPortion.getQuantityGrams();

                FoodPortion updatedPortion = new FoodPortion(
                        oldPortion.getFoodId(),
                        oldPortion.getFoodName(),
                        inputData.getNewGrams(),
                        oldPortion.getNutrition().scale(factor));

                meal.replacePortion(index, updatedPortion);
                totals = meal.getTotalNutrition();
            }
            catch (IllegalArgumentException exception) {
                presenter.prepareFailView(
                        "Food quantity is out of range.");
                return;
            }

            dataAccess.saveUpdatedPlan(updatedPlan);

            presenter.prepareSuccessView(
                    new ChangePlannedPortionOutputData(
                            updatedPlan.getDate(),
                            meal.getId(),
                            meal.getMealSlotId(),
                            totals.getCalories(),
                            totals.getProtein(),
                            totals.getCarbs(),
                            totals.getFat()));
        }
        catch (AddPlannedMealDataAccessException exception) {
            presenter.prepareFailView(
                    "Unable to save the planned meal. Please try again.");
        }
    }

    private DailyMealPlan copyPlan(DailyMealPlan original) {
        List<PlannedMeal> copiedMeals = new ArrayList<>();

        for (PlannedMeal meal : original.getPlannedMeals()) {
            copiedMeals.add(new PlannedMeal(
                    meal.getId(),
                    meal.getMealSlotId(),
                    meal.getPortions()));
        }

        return new DailyMealPlan(
                original.getId(),
                original.getDate(),
                copiedMeals);
    }
}