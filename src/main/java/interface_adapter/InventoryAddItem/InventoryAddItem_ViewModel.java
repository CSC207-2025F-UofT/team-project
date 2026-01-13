package interface_adapter.InventoryAddItem;

import interface_adapter.ViewModel;

public class InventoryAddItem_ViewModel extends ViewModel<InventoryAddItem_State> {
    public InventoryAddItem_ViewModel() {
        super("InventoryAddItem");
        setState(new InventoryAddItem_State());
    }

}
