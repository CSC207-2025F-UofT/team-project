package interface_adapter.toggle_bought;

import interface_adapter.shopping_list.ShoppingListPresenterHelper;
import interface_adapter.shopping_list.ShoppingListState;
import interface_adapter.shopping_list.ShoppingListViewModel;
import use_case.toggle_bought.ToggleBoughtOutputBoundary;
import use_case.toggle_bought.ToggleBoughtOutputData;

/**
 * Presents toggle-bought results into the shopping list ViewModel.
 */
public class ToggleBoughtPresenter implements ToggleBoughtOutputBoundary {

    private final ShoppingListViewModel viewModel;

    /**
     * Creates the presenter.
     *
     * @param viewModel the shopping list view model
     */
    public ToggleBoughtPresenter(ShoppingListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ToggleBoughtOutputData outputData) {
        ShoppingListState state = viewModel.getState();
        ShoppingListPresenterHelper.applyList(state, outputData.getShoppingList());
        state.setError("");
        state.setMessage("");
        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(ToggleBoughtOutputData outputData) {
        ShoppingListState state = viewModel.getState();
        state.setError(outputData.getMessage());
        state.setMessage("");
        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }
}
