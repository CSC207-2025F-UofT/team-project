package use_case.Inventory_UseItem;

import entity.Item;
import entity.User;

public class Inventory_UseItem_Interactor {
    private final Inventory_UseItemUserDataAccessInterface dataAccess;
    private final Inventory_UseItem_OutputBoundary outputBoundary;
    private User user;

    public Inventory_UseItem_Interactor(Inventory_UseItemUserDataAccessInterface dataAccess, Inventory_UseItem_OutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
    }
    /**
     * set user
     * @param user user of the game
     */
    public void setUser(User user) {
        this.user = user; }


    //Helper method map type to category like in the item interactor (again)
    private String mapTypeToCategory(String type) {
        if (type == null) return "heal";
        switch (type.toLowerCase()) {
            case "weapon":
            case "rod":
            case "wand":
            case "staff":
                return "weapon";
            case "armor":
            case "shield":
                return "armour";
            default:
                return "heal";
        }
    }

    /**
     *
     * @param inputDataUseItem item from inventory to be used
     */
    public void useItem(Inventory_InputData_UseItem inputDataUseItem) {
        if (user == null) {return;}
        // Item from data access
        //Item item = user.getInventory().getItemByName(inputDataUseItem.getItem_name());
        Item item = dataAccess.getItemByName(user, inputDataUseItem.getItemName());
        if (item == null) return;

        // updates stats based on hp
        int hp = 0, def = 0, dmg = 0;
        String category = mapTypeToCategory(item.getType());
        switch (category) {
            case "heal":
                hp = item.getValue();
                user.addBonusHP(hp);
                break;
            case "armour":
                def = Math.max(1, Math.min(4, Math.abs(item.getValue())-4));
                user.addDEF(def);
                break;
            case "weapon":
                int raw_dmg = item.getValue();
                dmg = (raw_dmg % 2 == 0) ? 2 : 1;
                user.addDMG(dmg);
                break;
        }
        dataAccess.removeItem(user, item);

        // updated inventory
        Inventory_UseItem_OutputData output = new Inventory_UseItem_OutputData(dataAccess.getInventory(user), hp, def, dmg);
        outputBoundary.useItem(output);
    }
    /**
     * viewing inventory (new)
     */
    public void viewInventory() {
        Inventory_UseItem_OutputData output = new Inventory_UseItem_OutputData(dataAccess.getInventory(user), 0, 0, 0);
    outputBoundary.presentInventory(output);
    }


}

