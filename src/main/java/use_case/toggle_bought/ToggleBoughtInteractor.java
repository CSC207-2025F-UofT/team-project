package use_case.toggle_bought;

import entity.IngredientName;
import entity.ShoppingList;

/**
 * Flips the bought flag for one shopping-list item and saves the list.
 */
public class ToggleBoughtInteractor implements ToggleBoughtInputBoundary {

    private static final String ITEM_NOT_FOUND = "Item is not on the shopping list.";
    private static final String LIST_NOT_FOUND = "No shopping list is saved for this week.";

    private final ToggleBoughtDataAccessInterface dataAccess;
    private final ToggleBoughtOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param dataAccess the shopping-list data access
     * @param presenter the output boundary
     */
    public ToggleBoughtInteractor(ToggleBoughtDataAccessInterface dataAccess,
                                  ToggleBoughtOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(ToggleBoughtInputData inputData) {
        ShoppingList list = dataAccess.loadShoppingList(inputData.getWeekStart());
        if (list == null) {
            presenter.prepareFailView(new ToggleBoughtOutputData(
                    null, LIST_NOT_FOUND, true));
            return;
        }

        IngredientName name = IngredientName.fromRawName(inputData.getIngredientName());
        try {
            list.toggleBought(name);
        }
        catch (IllegalArgumentException exception) {
            presenter.prepareFailView(new ToggleBoughtOutputData(
                    null, ITEM_NOT_FOUND, true));
            return;
        }

        dataAccess.saveShoppingList(list);
        presenter.prepareSuccessView(new ToggleBoughtOutputData(list, "", false));
    }
}
