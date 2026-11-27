package use_case.create_project;

public interface CreateProjectOutputBoundary {
    void prepareSuccessView(CreateProjectOutputData OutputData);
    void prepareFailView(String errorMessage);
}
