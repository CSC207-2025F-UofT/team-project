package use_case.addBook;

public interface AddBookOutputBoundary {
    /**
     * Prepares the success view for the Add Book use case
     * @param outputData the output data
     */
    void prepareSuccessView(AddBookOutputData outputData);

    /**
     * Prepares the failure view for the Add Book use case
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
