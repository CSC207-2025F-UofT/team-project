package use_case.meal_planning;

import use_case.edit_planned_meal.LoadPlannedPortionsOutputData;

public interface AddPlannedMealOutputBoundary {
    void prepareSuccessView(AddPlannedMealOutputData outputData);
    void prepareFailView(String errorMessage);
    void prepareOptionsView(AddPlannedMealOptionsOutputData outputData);
    void preparePortionsView(LoadPlannedPortionsOutputData outputData);
}
