package interface_adapter.InventoryUseItem;

import use_case.InventoryUseItem.InventoryUseItemOutputData;
import use_case.InventoryUseItem.InventoryUseItemOutputBoundary;
import entity.Inventory;

public class InventoryUseItemPresenter implements InventoryUseItemOutputBoundary {

    @Override
    public void useItem(InventoryUseItemOutputData outputData) {
        Inventory inventory = outputData.getInventory();
        int hp = outputData.getHpIncrease();
        int def = outputData.getDefIncrease();
        int dmg = outputData.getDmgIncrease();

        // test lines
        //System.out.print("Item used, has enhanced " );
        //if (hp > 0) { System.out.println("hp: " + hp); }
        //if (def > 0) { System.out.println("def: " + def); }
        //if (dmg > 0) { System.out.println("dmg: " + dmg); }

        //System.out.print("Item removed. Inventory has reduced to contain " );
        //for (Item item : items) {
          //  System.out.println("-" + item.getName() + "(" + item.getType() + ")"); }
    }

    @Override
    public void viewInventory(InventoryUseItemOutputData outputData) {
        Inventory inventory = outputData.getInventory();

        // test lines
        // System.out.print("Inventory contains: " );
        // for (Item item : inventory) {
        //    System.out.println("-" + item.getName() + "(" + item.getType() + ")"); }
    }
}
