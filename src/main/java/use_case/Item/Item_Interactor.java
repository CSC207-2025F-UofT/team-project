package use_case.Item;

import entity.Item;

public class Item_Interactor {
    /**
     * creates item object from string name
     */
    private final ItemUserDataAccessInterface itemDataAccess;

    public Item_Interactor(ItemUserDataAccessInterface itemDataAccessObject) {
    this.itemDataAccess = itemDataAccessObject;
    }

    /**
     * Item sorted by name
     * @param name the input item data contains item name
     */
    public Item getItemByName(String name) {
        return itemDataAccess.getItemByName(name); }



}


