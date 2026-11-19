package use_case.toggle_bought;

/**
 * Output boundary for toggling bought.
 */
public interface ToggleBoughtOutputBoundary {

    /**
     * Presents a successful toggle.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(ToggleBoughtOutputData outputData);

    /**
     * Presents a failed toggle.
     *
     * @param outputData the output data
     */
    void prepareFailView(ToggleBoughtOutputData outputData);
}
