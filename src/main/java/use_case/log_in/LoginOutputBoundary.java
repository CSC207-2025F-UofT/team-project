package use_case.log_in;

public interface LoginOutputBoundary {
    void prepareSuccessView(LoginOutputData outputData);
    void prepareFailView(String errorMessage);
}
