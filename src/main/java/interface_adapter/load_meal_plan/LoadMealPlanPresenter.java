package interface_adapter.load_meal_plan;

import interface_adapter.meal_plan.MealPlanPresenterHelper;
import interface_adapter.meal_plan.MealPlanState;
import interface_adapter.meal_plan.MealPlanViewModel;
import use_case.load_meal_plan.LoadMealPlanOutputBoundary;
import use_case.load_meal_plan.LoadMealPlanOutputData;

/**
 * Presenter that shows the loaded week on the meal plan page.
 */
public class LoadMealPlanPresenter implements LoadMealPlanOutputBoundary {

    private final MealPlanViewModel viewModel;

    /**
     * Creates the presenter.
     *
     * @param viewModel the meal plan view model
     */
    public LoadMealPlanPresenter(MealPlanViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(LoadMealPlanOutputData outputData) {
        MealPlanState state = viewModel.getState();
        MealPlanPresenterHelper.applyWeek(state, outputData.getWeek(), "");
        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }
}
