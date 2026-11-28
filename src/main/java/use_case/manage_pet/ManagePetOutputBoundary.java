package use_case.manage_pet;

/** Presents the result of a pet management action. */
public interface ManagePetOutputBoundary {

    /**
     * Displays the updated listings and action feedback.
     * @param outputData the result to display
     */
    void prepareSuccessView(ManagePetOutputData outputData);

    /**
     * Displays why the action could not be completed.
     * @param message a message for the admin
     */
    void prepareFailView(String message);
}
