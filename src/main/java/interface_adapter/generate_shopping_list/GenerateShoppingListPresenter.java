package interface_adapter.generate_shopping_list;

import interface_adapter.shopping_list.ShoppingListPresenterHelper;
import interface_adapter.shopping_list.ShoppingListState;
import interface_adapter.shopping_list.ShoppingListViewModel;
import use_case.generate_shopping_list.GenerateShoppingListOutputBoundary;
import use_case.generate_shopping_list.GenerateShoppingListOutputData;

/**
 * Presents generate-shopping-list results into the shopping list ViewModel.
 */
public class GenerateShoppingListPresenter implements GenerateShoppingListOutputBoundary {

    private final ShoppingListViewModel viewModel;

    /**
     * Creates the presenter.
     *
     * @param viewModel the shopping list view model
     */
    public GenerateShoppingListPresenter(ShoppingListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(GenerateShoppingListOutputData outputData) {
        ShoppingListState state = viewModel.getState();
        ShoppingListPresenterHelper.applyList(state, outputData.getShoppingList());
        state.setError("");
        state.setMessage(outputData.getMessage());
        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(GenerateShoppingListOutputData outputData) {
        ShoppingListState state = viewModel.getState();
        state.setError(outputData.getMessage());
        state.setMessage("");
        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }
}
