package use_case.save_document;

public interface SavingOutputBoundary {

    /**
     * Prepares the success view for the creating a document use case.
     * @param outputData the output data
     */
    void prepareSuccessView(SavingOutputData outputData);
}
