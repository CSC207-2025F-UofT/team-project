package use_case.log_input;

/**
 * Input Boundary Interface for the Log Input Use Case.
 */
public interface LogInputInputBoundary {
    /**
     * Executes the Log Input use case.
     * @param logInputInputData the input data
     */
    void execute(LogInputInputData logInputInputData);
}
