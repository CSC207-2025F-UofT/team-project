package use_case.meal_planning;

import entity.DailyMealPlan;
import entity.MealSchedule;
import entity.MealSlot;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Creates a daily meal plan and its configurable meal schedule.
 */
public class CreatePlanInteractor implements CreatePlanInputBoundary {

    private final CreatePlanDataAccessInterface dataAccess;
    private final CreatePlanOutputBoundary presenter;

    public CreatePlanInteractor(
            CreatePlanDataAccessInterface dataAccess,
            CreatePlanOutputBoundary presenter) {
        this.dataAccess = Objects.requireNonNull(dataAccess);
        this.presenter = Objects.requireNonNull(presenter);
    }

    @Override
    public void execute(CreatePlanInputData inputData) {
        if (inputData == null || inputData.getDate() == null) {
            presenter.prepareFailView("Please select a date.");
            return;
        }

        List<String> slotNames = inputData.getMealSlotNames();

        if (slotNames == null) {
            presenter.prepareFailView("Meal slot names are required.");
            return;
        }

        for (String name : slotNames) {
            if (name == null || name.isBlank()) {
                presenter.prepareFailView(
                        "Meal slot names cannot be empty.");
                return;
            }
        }

        DailyMealPlan plan;

        try {
            if (dataAccess.existsByDate(inputData.getDate())) {
                presenter.prepareFailView(
                        "A meal plan already exists for this date.");
                return;
            }

            plan = new DailyMealPlan(
                    UUID.randomUUID().toString(),
                    inputData.getDate()
            );

            MealSchedule schedule = new MealSchedule();

            for (String name : slotNames) {
                MealSlot slot = new MealSlot(
                        UUID.randomUUID().toString(),
                        name.strip()
                );
                schedule.addSlot(slot);
            }

            dataAccess.save(plan, schedule);

        } catch (CreatePlanDataAccessException exception) {
            presenter.prepareFailView(
                    "Unable to access meal plan storage. Please try again.");
            return;
        }

        CreatePlanOutputData outputData = new CreatePlanOutputData(
                plan.getId(),
                plan.getDate()
        );

        presenter.prepareSuccessView(outputData);
    }
}
