package use_case.communicate_request;

/**
 * Input boundary for UC1 - Build a Request.
 */
public interface CommunicateRequestInputBoundary {

    /**
     * Loads the current communication board.
     */
    void loadBoard();

    /**
     * Appends one pictogram to the current draft.
     */
    void selectPictogram(int pictogramId);

    /**
     * Removes the most recently selected pictogram.
     */
    void removeLastSelection();

    /**
     * Clears the current draft.
     */
    void clearDraft();
}