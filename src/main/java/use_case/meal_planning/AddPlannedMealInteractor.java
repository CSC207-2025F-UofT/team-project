package use_case.meal_planning;

import entity.DailyMealPlan;
import entity.FoodItem;
import entity.FoodPortion;
import entity.MealSchedule;
import entity.NutritionTotals;
import entity.PlannedMeal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AddPlannedMealInteractor implements AddPlannedMealInputBoundary {

    private final AddPlannedMealDataAccessInterface dataAccess;
    private final AddPlannedMealOutputBoundary presenter;

    public AddPlannedMealInteractor(
            AddPlannedMealDataAccessInterface dataAccess,
            AddPlannedMealOutputBoundary presenter) {
        this.dataAccess = Objects.requireNonNull(dataAccess);
        this.presenter = Objects.requireNonNull(presenter);
    }


    @Override
    public void loadOptions(LocalDate date) {
        if (date == null) {
            presenter.prepareFailView("Please select a date.");
            return;
        }

        try {
            DailyMealPlan plan = dataAccess.findPlanByDate(date);
            if (plan == null) {
                presenter.prepareFailView(
                        "No meal plan exists for this date.");
                return;
            }

            MealSchedule schedule =
                    dataAccess.findScheduleByPlanId(plan.getId());
            if (schedule == null) {
                presenter.prepareFailView(
                        "Meal slots are unavailable for this date.");
                return;
            }

            List<AddPlannedMealOption> slotOptions =
                    new ArrayList<>();
            for (entity.MealSlot slot : schedule.getSlots()) {
                slotOptions.add(new AddPlannedMealOption(
                        slot.getId(), slot.getName()));
            }

            List<AddPlannedMealOption> foodOptions =
                    new ArrayList<>();
            for (FoodItem food : dataAccess.findAllFoods()) {
                foodOptions.add(new AddPlannedMealOption(
                        food.getId(), food.getName()));
            }
            foodOptions.sort(Comparator.comparing(
                    AddPlannedMealOption::getName,
                    String.CASE_INSENSITIVE_ORDER));

            presenter.prepareOptionsView(
                    new AddPlannedMealOptionsOutputData(
                            date, slotOptions, foodOptions));

        }
        catch (AddPlannedMealDataAccessException exception) {
            presenter.prepareFailView(
                    "Unable to load meal options. Please try again.");
        }
    }


    @Override
    public void execute(AddPlannedMealInputData inputData) {
        if (inputData == null || inputData.getDate() == null) {
            presenter.prepareFailView("Please select a date.");
            return;
        }

        String slotIds = inputData.getMealSlotId();

        if (slotIds == null) {
            presenter.prepareFailView("Please select a meal slot");
            return;
        }

        if (inputData.getFoodId() == null
                || inputData.getFoodId().isBlank()) {
            presenter.prepareFailView("Please select a food.");
            return;
        }

        if (!Double.isFinite(inputData.getGrams())
                || inputData.getGrams() <= 0) {
            presenter.prepareFailView("Food quantity must be greater than zero.");
            return;
        }

        try{
            DailyMealPlan storedPlan = dataAccess.findPlanByDate(inputData.getDate());
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

            FoodItem food =
                    dataAccess.findFoodById(inputData.getFoodId());
            if (food == null) {
                presenter.prepareFailView(
                        "The selected food does not exist.");
                return;
            }

            // Work on a copy so a failed save does not change storedPlan.
            DailyMealPlan updatedPlan = copyPlan(storedPlan);
            PlannedMeal meal = updatedPlan.getMealBySlotId(
                    inputData.getMealSlotId());

            if (meal == null) {
                meal = new PlannedMeal(
                        UUID.randomUUID().toString(),
                        inputData.getMealSlotId());
                updatedPlan.addMeal(meal);
            }

            FoodPortion portion;
            try {
                portion = FoodPortion.from(
                        food, inputData.getGrams());
            }
            catch (IllegalArgumentException exception) {
                presenter.prepareFailView(
                        "Food quantity is invalid.");
                return;
            }

            meal.addPortion(portion);
            NutritionTotals totals = meal.getTotalNutrition();

            dataAccess.saveUpdatedPlan(updatedPlan);

            presenter.prepareSuccessView(
                    new AddPlannedMealOutputData(
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




