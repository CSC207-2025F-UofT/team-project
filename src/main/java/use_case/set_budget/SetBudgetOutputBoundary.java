package use_case.set_budget;

/**
 * The output boundary for the Set Budget use case.
 */
public interface SetBudgetOutputBoundary {
    /**
     * Prepares the success view for the Set Budget use case.
     * @param outputData the output data
     */
    void prepareSuccessView(SetBudgetOutputData outputData);

    /**
     * Prepares the failure view for the Set Budget use case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}