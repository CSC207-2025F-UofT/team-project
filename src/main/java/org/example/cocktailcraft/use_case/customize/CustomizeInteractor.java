package org.example.cocktailcraft.use_case.customize;

/** Scaffold only. TODO(Enhao): inject the DAO and implement business rules. */
public class CustomizeInteractor implements CustomizeInputBoundary {
    private final CustomizeOutputBoundary presenter;

    public CustomizeInteractor(CustomizeOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(CustomizeInputData inputData) {
        // TODO(Enhao): validate input, call the data access interface, and produce output.
        presenter.prepareFailView("Not implemented: Customize (Enhao).");
    }
}
