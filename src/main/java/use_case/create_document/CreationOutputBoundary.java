package use_case.create_document;

public interface CreationOutputBoundary {

    /**
     * Prepares the success view for the creating a document use case.
     * @param outputData the output data
     */
    void prepareSuccessView(CreationOutputData outputData);

    /**
     * Prepares the failure view for the CreatFile Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
