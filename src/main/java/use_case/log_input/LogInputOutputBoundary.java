package use_case.log_input;

/**
 * The output boundary for the Log Input Use Case.
 */
public interface LogInputOutputBoundary {
    /**
     * Prepares the success view for the Log Input Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(LogInputOutputData outputData);

    /**
     * Prepares the failure view for the Log Input Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
