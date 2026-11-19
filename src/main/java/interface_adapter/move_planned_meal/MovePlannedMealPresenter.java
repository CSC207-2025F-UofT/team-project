package interface_adapter.move_planned_meal;

import interface_adapter.meal_plan.MealPlanPresenterHelper;
import interface_adapter.meal_plan.MealPlanState;
import interface_adapter.meal_plan.MealPlanViewModel;
import use_case.move_planned_meal.MovePlannedMealOutputBoundary;
import use_case.move_planned_meal.MovePlannedMealOutputData;

/**
 * Presenter that updates the meal plan page after a meal is moved.
 */
public class MovePlannedMealPresenter implements MovePlannedMealOutputBoundary {

    private final MealPlanViewModel viewModel;

    /**
     * Creates the presenter.
     *
     * @param viewModel the meal plan view model
     */
    public MovePlannedMealPresenter(MealPlanViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(MovePlannedMealOutputData outputData) {
        MealPlanState state = viewModel.getState();
        MealPlanPresenterHelper.applyPlan(state, outputData.getMealPlan(),
                "Moved " + outputData.getMovedRecipeName() + ".");
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
