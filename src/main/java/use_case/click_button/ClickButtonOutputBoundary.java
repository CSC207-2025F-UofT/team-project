package use_case.click_button;

/**
 * Output boundary for the Click Button use case.
 */
public interface ClickButtonOutputBoundary {

    /**
     * Prepares the view with the button click result.
     * @param outputData the output data containing the message to display
     */
    void prepareView(ClickButtonOutputData outputData);
}
