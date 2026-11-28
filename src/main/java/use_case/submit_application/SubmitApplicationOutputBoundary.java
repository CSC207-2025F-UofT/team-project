package use_case.submit_application;

/**
 * The Output Boundary for the Submit Application Use Case.
 */
public interface SubmitApplicationOutputBoundary {

    /**
     * Prepares the success view.
     * @param outputData the output data
     */
    void prepareSuccessView(SubmitApplicationOutputData outputData);

    /**
     * Prepares the failure view.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
