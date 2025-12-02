import entity.Item;
import entity.User;
import org.junit.Test;
import use_case.InventoryAddItem.InventoryAddItemOutputBoundary;
import use_case.InventoryAddItem.InventoryAddItemOutputData;
import use_case.InventoryAddItem.InventoryAddItemInteractor;
import use_case.InventoryAddItem.InventoryAddItemInputData;

import static org.junit.Assert.*;

public class AddItemInteractorTest {
    // Test with all expected information given/no early return
    // (for standard use)

    @Test
    public void addItemStandardTest() {
        User user = new User();
        Item item = new Item("Blades of Frost", "Weapon");

        InventoryAddItemOutputBoundary output = new InventoryAddItemOutputBoundary() {
            @Override
            public void present(InventoryAddItemOutputData data) {
                assertEquals(item, data.getItem());
                assertTrue(data.getInventory().getItems().contains(item));
            }
        };

        InventoryAddItemInteractor interactor = new InventoryAddItemInteractor(output);
        interactor.setUser(user);

        InventoryAddItemInputData input = new InventoryAddItemInputData(item);
        interactor.addItem(input);

        assertTrue(user.getInventory().getItems().contains(item));


    }
    // add item user is null

    @Test
    public void addItemUserNull() {
        Item item = new Item("Blades of Frost", "Weapon");

        InventoryAddItemOutputBoundary output = new InventoryAddItemOutputBoundary() {
            @Override
            public void present(InventoryAddItemOutputData data) {
                fail("Output should not be called when user is null");
            }
        };

        InventoryAddItemInteractor interactor = new InventoryAddItemInteractor(output);

        interactor.addItem(new InventoryAddItemInputData(item));
    }

    //  add item input is null
    @Test
    public void addItemInputNull() {
        User user = new User();

        InventoryAddItemOutputBoundary output = new InventoryAddItemOutputBoundary() {
            @Override
            public void present(InventoryAddItemOutputData data) {
                fail("Output should not be called when inputData is null");
            }
        };

        InventoryAddItemInteractor interactor = new InventoryAddItemInteractor(output);
        interactor.setUser(user);

        interactor.addItem(null);
    }



    // add item is null test
    @Test
    public void addItemItemNull() {
        User user = new User();

        InventoryAddItemInputData input = new InventoryAddItemInputData(null);

        InventoryAddItemOutputBoundary output = new InventoryAddItemOutputBoundary() {
            @Override
            public void present(InventoryAddItemOutputData data) {
                fail("Output should not be called when item is null");
            }
        };

        InventoryAddItemInteractor interactor = new InventoryAddItemInteractor(output);
        interactor.setUser(user);

        interactor.addItem(input);
    }




}
