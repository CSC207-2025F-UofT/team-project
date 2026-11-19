package use_case.update_stock_level;

/**
 * Output boundary for changing a pantry item's stock level.
 */
public interface UpdateStockLevelOutputBoundary {

    /**
     * Presents a successful update.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(UpdateStockLevelOutputData outputData);

    /**
     * Presents a failed update.
     *
     * @param outputData the output data
     */
    void prepareFailView(UpdateStockLevelOutputData outputData);
}
