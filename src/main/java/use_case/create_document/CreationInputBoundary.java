package use_case.create_document;

/**
 * Input Boundary for actions which are related to signing up.
 */
public interface CreationInputBoundary {

    /**
     * Executes the creating a file use case.
     * @param creationInputData the input data
     */
    void execute(CreationInputData creationInputData);

}
