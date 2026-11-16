package use_case.save_session;

/**
 * The input boundary for the Save Session Use Case.
 */
public interface SaveSessionInputBoundary {

    /**
     * Execute the Save Session Use Case.
     * @param inputData the input data for this use case
     */
    void execute(SaveSessionInputData inputData);
}
