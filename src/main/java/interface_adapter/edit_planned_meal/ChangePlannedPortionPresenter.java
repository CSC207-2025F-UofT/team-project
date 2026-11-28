package interface_adapter.edit_planned_meal;


import interface_adapter.meal_planning.AddPlannedMealViewModel;
import use_case.edit_planned_meal.ChangePlannedPortionOutputBoundary;
import use_case.edit_planned_meal.ChangePlannedPortionOutputData;

import java.util.Locale;
import java.util.Objects;

public class ChangePlannedPortionPresenter
        implements ChangePlannedPortionOutputBoundary {

    private final AddPlannedMealViewModel viewModel;

    public ChangePlannedPortionPresenter(
            AddPlannedMealViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel);
    }

    @Override
    public void prepareSuccessView(
            ChangePlannedPortionOutputData outputData) {
        String message = String.format(
                Locale.US,
                "Portion updated. Meal total: %.1f kcal, "
                        + "%.1f g protein.",
                outputData.getCalories(),
                outputData.getProtein());

        viewModel.update(message, true);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.update(errorMessage, false);
    }
}