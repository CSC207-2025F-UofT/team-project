package use_case.toggle_bought;

/**
 * Input boundary for toggling bought on a shopping item.
 */
public interface ToggleBoughtInputBoundary {

    /**
     * Toggles the bought flag for one item and saves the list.
     *
     * @param inputData the input data
     */
    void execute(ToggleBoughtInputData inputData);
}
