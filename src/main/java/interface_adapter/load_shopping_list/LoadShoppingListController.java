package interface_adapter.load_shopping_list;

import use_case.load_shopping_list.LoadShoppingListInputBoundary;
import use_case.load_shopping_list.LoadShoppingListInputData;

import java.time.LocalDate;

/**
 * Controller for loading a shopping list.
 */
public class LoadShoppingListController {

    private final LoadShoppingListInputBoundary interactor;

    /**
     * Creates the controller.
     *
     * @param interactor the load interactor
     */
    public LoadShoppingListController(LoadShoppingListInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Loads the shopping list for the given week.
     *
     * @param weekStart the Monday that starts the week
     */
    public void execute(LocalDate weekStart) {
        interactor.execute(new LoadShoppingListInputData(weekStart));
    }
}
