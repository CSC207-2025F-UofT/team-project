package use_case.login;

public interface LoginOutputBoudary {
    void prepareSuccessView(LoginOutputData loginOutputData);
    void prepareFailureView(String error);
}
