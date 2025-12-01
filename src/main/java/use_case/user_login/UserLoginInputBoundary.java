package use_case.user_login;

public interface UserLoginInputBoundary {

    /**
     * Executes the login use case.
     * @param loginInputData the input data
     */
    void execute(UserLoginInputData loginInputData);
}

