package interface_adapter.InventoryAddItem;

import entity.Item;
import use_case.InventoryAddItem.InventoryAddItemOutputBoundary;
import use_case.InventoryAddItem.InventoryAddItemOutputData;

/**
 * Presenter for AddItem Usecase
 * updates viewModel state with added item.
 */

public class InventoryAddItemPresenter implements InventoryAddItemOutputBoundary {
    private final InventoryAddItemViewModel viewModel;

    public InventoryAddItemPresenter(InventoryAddItemViewModel viewModel) {
        this.viewModel = viewModel; }

    @Override
    public void present(InventoryAddItemOutputData outputData) {
        Item item = outputData.getItem();
        viewModel.getState().setAddedItem(item);


    }
}
