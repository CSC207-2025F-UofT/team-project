package use_case.Inventory_UseItem;

import entity.Inventory;


/**
 * Updates the user's stats based on the item type
 */
public class Inventory_UseItem_OutputData {
    private final Inventory inventory;
    private final int hpIncrease;
    private final int defIncrease;
    private final int dmgIncrease;
    public Inventory_UseItem_OutputData(Inventory inventory, int hpIncrease , int defIncrease , int dmgIncrease ) {
        this.inventory = inventory;
        this.hpIncrease = hpIncrease;
        this.defIncrease = defIncrease;
        this.dmgIncrease = dmgIncrease;
    }

    public Inventory getInventory() {
        return inventory; }
    public int getHpIncrease() { return hpIncrease;}
    public int getDefIncrease() { return defIncrease;}
    public int getDmgIncrease() { return dmgIncrease;}
}
