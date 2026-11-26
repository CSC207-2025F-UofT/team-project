package user_case.Login;

public interface LoginOutputBoundary {
    void PrepareSuccessView(LoginOutputData loginOutputData);
    void PrepareFailView(String errorMessages);
}
