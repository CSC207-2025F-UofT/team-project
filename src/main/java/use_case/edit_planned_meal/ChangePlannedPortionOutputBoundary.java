package use_case.edit_planned_meal;

public interface ChangePlannedPortionOutputBoundary {
    void prepareSuccessView(ChangePlannedPortionOutputData outputData);

    void prepareFailView(String errorMessage);
}
