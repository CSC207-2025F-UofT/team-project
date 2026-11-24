package use_case.meal_planning;

public interface AddPlannedMealOutputBoundary {
    void prepareSuccessView(AddPlannedMealOutputData outputData);
    void prepareFailView(String errorMessage);
    void prepareOptionsView(AddPlannedMealOptionsOutputData outputData);
}
