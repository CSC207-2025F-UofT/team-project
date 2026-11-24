package use_case.add_pantry_item;

/**
 * Output boundary for adding an item to the pantry.
 */
public interface AddPantryItemOutputBoundary {

    /**
     * Presents a successful add operation.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(AddPantryItemOutputData outputData);

    /**
     * Presents a failed add operation.
     *
     * @param outputData the output data
     */
    void prepareFailView(AddPantryItemOutputData outputData);
}
