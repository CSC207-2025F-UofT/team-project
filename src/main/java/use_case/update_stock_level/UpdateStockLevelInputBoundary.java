package use_case.update_stock_level;

/**
 * Input boundary for changing a pantry item's stock level.
 */
public interface UpdateStockLevelInputBoundary {

    /**
     * Changes one pantry item's stock level.
     *
     * @param inputData the input data
     */
    void execute(UpdateStockLevelInputData inputData);
}
