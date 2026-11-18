package interface_adapter.generate_shopping_list;

import use_case.generate_shopping_list.GenerateShoppingListInputBoundary;
import use_case.generate_shopping_list.GenerateShoppingListInputData;

/**
 * Controller for generating a shopping list.
 */
public class GenerateShoppingListController {

    private final GenerateShoppingListInputBoundary interactor;

    /**
     * Creates the controller.
     *
     * @param interactor the generate interactor
     */
    public GenerateShoppingListController(GenerateShoppingListInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Generates a shopping list for the current week.
     */
    public void execute() {
        interactor.execute(new GenerateShoppingListInputData());
    }
}
