package use_case.load_shopping_list;

import entity.ShoppingList;

import java.util.List;

/**
 * Loads the saved shopping list for a week, or an empty list if none exists.
 */
public class LoadShoppingListInteractor implements LoadShoppingListInputBoundary {

    private final LoadShoppingListDataAccessInterface dataAccess;
    private final LoadShoppingListOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param dataAccess the shopping-list data access
     * @param presenter the output boundary
     */
    public LoadShoppingListInteractor(LoadShoppingListDataAccessInterface dataAccess,
                                      LoadShoppingListOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoadShoppingListInputData inputData) {
        ShoppingList saved = dataAccess.loadShoppingList(inputData.getWeekStart());
        boolean empty = saved == null;
        ShoppingList toShow = empty
                ? new ShoppingList(inputData.getWeekStart(), List.of())
                : saved;
        presenter.prepareSuccessView(new LoadShoppingListOutputData(toShow, empty));
    }
}
