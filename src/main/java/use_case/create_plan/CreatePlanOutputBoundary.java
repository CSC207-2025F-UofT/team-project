package use_case.create_plan;

public interface CreatePlanOutputBoundary {
    void prepareSuccessView(CreatePlanOutputData outputData);
    void prepareFailView(String errorMessage);
}
