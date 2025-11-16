package use_case.change_username;

public interface ChangeUsernameOutputBoundary {
    void prepareSuccessView(ChangeUsernameOutputData user);
    void prepareFailView(String error);
}