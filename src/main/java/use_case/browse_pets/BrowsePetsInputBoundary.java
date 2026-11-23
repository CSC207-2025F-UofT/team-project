package use_case.browse_pets;

/**
 * Entry point of the browse pets use case, called by the controller.
 */
public interface BrowsePetsInputBoundary {

    /**
     * Finds the available pets that pass every filter.
     *
     * @param inputData the filters chosen by the visitor
     */
    void execute(BrowsePetsInputData inputData);
}
