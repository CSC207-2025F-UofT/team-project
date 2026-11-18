package interface_adapter.load_shopping_list;

import interface_adapter.shopping_list.ShoppingListPresenterHelper;
import interface_adapter.shopping_list.ShoppingListState;
import interface_adapter.shopping_list.ShoppingListViewModel;
import use_case.load_shopping_list.LoadShoppingListOutputBoundary;
import use_case.load_shopping_list.LoadShoppingListOutputData;

/**
 * Presents loaded shopping-list data into the shopping list ViewModel.
 */
public class LoadShoppingListPresenter implements LoadShoppingListOutputBoundary {

    private final ShoppingListViewModel viewModel;

    /**
     * Creates the presenter.
     *
     * @param viewModel the shopping list view model
     */
    public LoadShoppingListPresenter(ShoppingListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(LoadShoppingListOutputData outputData) {
        ShoppingListState state = viewModel.getState();
        ShoppingListPresenterHelper.applyList(state, outputData.getShoppingList());
        state.setError("");
        state.setMessage("");
        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }
}
