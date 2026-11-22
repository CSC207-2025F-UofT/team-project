package use_case.create_document;

/**
 * Input Boundary for actions which are related to creating a document.
 */
public interface CreationInputBoundary {

    /**
     * Executes the creating a document use case.
     * @param creationInputData the input data
     */
    void execute(CreationInputData creationInputData);

    void switchToNewDocumentView();

}
