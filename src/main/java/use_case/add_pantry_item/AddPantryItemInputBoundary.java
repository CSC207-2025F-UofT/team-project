package use_case.add_pantry_item;

/**
 * Input boundary for adding an item to the pantry.
 */
public interface AddPantryItemInputBoundary {

    /**
     * Adds one pantry item.
     *
     * @param inputData the input data
     */
    void execute(AddPantryItemInputData inputData);
}
