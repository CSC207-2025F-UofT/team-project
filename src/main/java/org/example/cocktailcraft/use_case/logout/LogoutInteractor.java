package org.example.cocktailcraft.use_case.logout;

/** Scaffold only. TODO(Jiaqi): inject the DAO and implement business rules. */
public class LogoutInteractor implements LogoutInputBoundary {
    private final LogoutOutputBoundary presenter;

    public LogoutInteractor(LogoutOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        // TODO(Jiaqi): validate input, call the data access interface, and produce output.
        presenter.prepareFailView("Not implemented: Logout (Jiaqi).");
    }
}
