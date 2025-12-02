package interface_adapter.InventoryAddItem;

import interface_adapter.ViewModel;

public class InventoryAddItemViewModel extends ViewModel<InventoryAddItemState> {
    public InventoryAddItemViewModel() {
        super("InventoryAddItem");
        setState(new InventoryAddItemState());
    }

}
