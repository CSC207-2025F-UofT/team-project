package use_case.user_login;

public interface UserLoginOutputBoundary {

    void prepareSuccessView(UserLoginOutputData outputData);
    // Prepares the success view for the Login Use Case.

    void prepareFailView(String errorMessage);
    // Prepares the failure view for the Login Use Case.
}
