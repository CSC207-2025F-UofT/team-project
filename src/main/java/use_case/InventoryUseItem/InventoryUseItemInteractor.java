package use_case.InventoryUseItem;

import entity.Item;
import entity.User;

public class InventoryUseItemInteractor implements InventoryUseItemInputBoundary {
    private final InventoryUseItemOutputBoundary outputBoundary;
    private User user;

    public InventoryUseItemInteractor(InventoryUseItemOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }
    /**
     * set user
     * @param user user of the game
     */
    @Override
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
            case "ring":
                return "armour";
            default:
                return "heal"; } }

    /**
     * @param inputDataUseItem item from inventory to be used
     */
    @Override
    public void useItem(InventoryUseItemInputData inputDataUseItem) {
        if (user == null || inputDataUseItem == null) {return;}
        // Item from data access
        Item item = user.getItemByName(inputDataUseItem.getItemName());
        if (item == null) {return;}
        int hp = 0, def = 0, dmg = 0;

        // updates stats based on hp
        String category = mapTypeToCategory(item.getType());
        switch (category) {
            case "heal":
                hp = Math.max(1,item.getValue());
                user.addBonusHP(hp);
                break;
            case "armour":
                def = Math.max(1, Math.min(4, Math.abs(item.getValue())-4));
                user.addDEF(def);
                break;
            case "weapon":
                dmg = (item.getValue() % 2 == 0) ? 2 : 1;
                user.addDMG(dmg);
                break;
        }

        user.removeItem(item);

        // updated inventory
        InventoryUseItemOutputData output = new InventoryUseItemOutputData(user.getInventory(), hp, def, dmg);
        outputBoundary.useItem(output);
    }
    /**
     * viewing inventory
     */
    @Override
    public void viewInventory() {
        InventoryUseItemOutputData output = new InventoryUseItemOutputData(user.getInventory(), 0, 0, 0);
    outputBoundary.viewInventory(output);
    }



}

