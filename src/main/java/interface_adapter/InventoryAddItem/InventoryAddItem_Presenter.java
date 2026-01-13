package interface_adapter.InventoryAddItem;

import entity.Item;
import use_case.Inventory_AddItem.Inventory_AddItem_OutputBoundary;
import use_case.Inventory_AddItem.Inventory_AddItem_OutputData;

/**
 * Presenter for AddItem Usecase
 * updates viewModel state with added item.
 */

public class InventoryAddItem_Presenter implements Inventory_AddItem_OutputBoundary {
    private final InventoryAddItem_ViewModel viewModel;

    public InventoryAddItem_Presenter(InventoryAddItem_ViewModel viewModel) {
        this.viewModel = viewModel; }

    @Override
    public void present(Inventory_AddItem_OutputData outputData) {
        Item item = outputData.getItem();
        viewModel.getState().setAddedItem(item);


    }
}
