package use_case.get_data;

public interface get_dataOutputBoundary {
    /**
     * Prepares the success view for the get_data Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(get_dataOutputData outputData);

    /**
     * Prepares the failure view for the get_data Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

}
