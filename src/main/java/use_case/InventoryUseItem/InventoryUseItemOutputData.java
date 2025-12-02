package use_case.InventoryUseItem;

import entity.Inventory;


/**
 * Updates the user's stats based on the item type
 */
public class InventoryUseItemOutputData {
    private final Inventory inventory;
    private final int hpIncrease;
    private final int defIncrease;
    private final int dmgIncrease;
    public InventoryUseItemOutputData(Inventory inventory, int hpIncrease , int defIncrease , int dmgIncrease ) {
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
