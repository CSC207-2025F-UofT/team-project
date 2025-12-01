package interface_adapter.InventoryUseItem;

import use_case.Inventory_UseItem.Inventory_UseItem_OutputData;
import use_case.Inventory_UseItem.Inventory_UseItem_OutputBoundary;
import entity.Item;
import entity.Inventory;
import java.util.List;

public class InventoryUseItem_Presenter implements Inventory_UseItem_OutputBoundary {

    @Override
    public void useItem(Inventory_UseItem_OutputData outputData) {
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
    public void presentInventory(Inventory_UseItem_OutputData outputData) {
        Inventory inventory = outputData.getInventory();

        // test lines
        // System.out.print("Inventory contains: " );
        // for (Item item : inventory) {
        //    System.out.println("-" + item.getName() + "(" + item.getType() + ")"); }
    }
}
