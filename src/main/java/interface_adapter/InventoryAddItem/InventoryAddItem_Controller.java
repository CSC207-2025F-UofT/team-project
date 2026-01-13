package interface_adapter.InventoryAddItem;

import entity.User;

import use_case.Inventory_AddItem.Inventory_AddItem_Interactor;
import use_case.Inventory_AddItem.Inventory_InputData_AddItem;

/**
 * Controller for AddItem usecase
 * Gets user input
 */
public class InventoryAddItem_Controller {
    private final Inventory_AddItem_Interactor interactor;

    public InventoryAddItem_Controller(Inventory_AddItem_Interactor interactor) {
        this.interactor = interactor;
    }

    /**
     *
     * @param user inventory of user to be updated
     * @param item name of item to be added
     */
    public void addItem(User user, String item) {
        interactor.setUser(user);
        Inventory_InputData_AddItem inputData = new Inventory_InputData_AddItem(item);
        interactor.addItem(inputData);


    }


}
