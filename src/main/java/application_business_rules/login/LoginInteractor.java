package application_business_rules.login;

/** TODO: Binwen implements authentication and failure handling here. */
public final class LoginInteractor implements LoginInputBoundary {
    private final LoginDataAccessInterface dataAccess;
    private final LoginOutputBoundary presenter;

    public LoginInteractor(LoginDataAccessInterface dataAccess, LoginOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoginInputData inputData) {
        throw new UnsupportedOperationException("Login use case not implemented yet.");
    }
}
