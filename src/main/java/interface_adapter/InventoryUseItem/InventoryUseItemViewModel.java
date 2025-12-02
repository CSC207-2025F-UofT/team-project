package interface_adapter.InventoryUseItem;
import interface_adapter.ViewModel;

/**
 * view model sets state of use item
 */

public class InventoryUseItemViewModel extends ViewModel<InventoryUseItemState> {
    public InventoryUseItemViewModel( ) {
        super("InventoryUseItemState");
        setState(new InventoryUseItemState());
    }
}

