package interface_adapter.remove_planned_meal;

import interface_adapter.meal_plan.MealPlanPresenterHelper;
import interface_adapter.meal_plan.MealPlanState;
import interface_adapter.meal_plan.MealPlanViewModel;
import use_case.remove_planned_meal.RemovePlannedMealOutputBoundary;
import use_case.remove_planned_meal.RemovePlannedMealOutputData;

/**
 * Presenter that updates the meal plan page after a meal is removed.
 */
public class RemovePlannedMealPresenter implements RemovePlannedMealOutputBoundary {

    private final MealPlanViewModel viewModel;

    /**
     * Creates the presenter.
     *
     * @param viewModel the meal plan view model
     */
    public RemovePlannedMealPresenter(MealPlanViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(RemovePlannedMealOutputData outputData) {
        MealPlanState state = viewModel.getState();
        MealPlanPresenterHelper.applyWeek(state, outputData.getWeek(),
                "Removed " + outputData.getRemovedRecipeName() + ".");
        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        MealPlanState state = viewModel.getState();
        state.setMessage("");
        state.setError(errorMessage);
        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }
}
