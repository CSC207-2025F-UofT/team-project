package use_case.save_document;

/**
 * Input Boundary for actions which are related to signing up.
 */
public interface SavingInputBoundary {

    /**
     * Executes the creating a file use case.
     * @param creationInputData the input data
     */
    void execute(SavingInputData creationInputData);

}
