package org.example.cocktailcraft.use_case.login;

/** Scaffold only. TODO(Jiaqi): inject the DAO and implement business rules. */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginOutputBoundary presenter;

    public LoginInteractor(LoginOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(LoginInputData inputData) {
        // TODO(Jiaqi): validate input, call the data access interface, and produce output.
        presenter.prepareFailView("Not implemented: Login (Jiaqi).");
    }
}
