package org.example.cocktailcraft.use_case.click;

public interface ClickOutputBoundary {
    /**
     * Prepares the success view for the Click Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(ClickOutputData outputData);

    /**
     * Prepares the failure view for the Click Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
