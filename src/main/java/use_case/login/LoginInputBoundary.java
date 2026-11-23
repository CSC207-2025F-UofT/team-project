package use_case.login;


public interface LoginInputBoundary {
    void execute(LoginInputBoundary inputData);
    void switchToLoginView();
}
