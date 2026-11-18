package use_case.submit_application;

/**
 * Input Boundary for the Submit Application Use Case.
 */
public interface SubmitApplicationInputBoundary {

    /**
     * Executes the submit application use case.
     * @param inputData the input data
     */
    void execute(SubmitApplicationInputData inputData);
}
