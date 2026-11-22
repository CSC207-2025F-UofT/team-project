package application_business_rules.login;

public interface LoginOutputBoundary {
    void prepareSuccessView(LoginOutputData outputData);
    void prepareFailView(String error);
}
