package org.example.cocktailcraft.use_case.signup;

/** Scaffold only. TODO(Jiaqi): inject the DAO and implement business rules. */
public class SignupInteractor implements SignupInputBoundary {
    private final SignupOutputBoundary presenter;

    public SignupInteractor(SignupOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(SignupInputData inputData) {
        // TODO(Jiaqi): validate input, call the data access interface, and produce output.
        presenter.prepareFailView("Not implemented: Signup (Jiaqi).");
    }
}
