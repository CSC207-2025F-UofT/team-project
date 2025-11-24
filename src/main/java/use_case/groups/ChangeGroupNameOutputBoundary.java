package use_case.groups;

public interface ChangeGroupNameOutputBoundary {
    void prepareSuccessView(ChangeGroupNameOutputData outputData);
    void prepareFailView(ChangeGroupNameOutputData outputData);
}