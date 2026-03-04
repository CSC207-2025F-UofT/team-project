package entity;

public class Item {
    /**
     * Item class will be a class with name, description, type, value.
     *  Item will be used by the Inventory class which is a list of items.
     *  Items will be used by the user to add buffs, and items will be added to the inventory by the user.
     */

    private String name;
    private String description;
    private String type;
    private int value;

    public Item(String name, String type) {
        this.name = name;
        this.type = type;
        int len = name.length() / 3;
        if (len > 11) { value = 11; } else { value = len; }
        this.description = name + " is a " + type + " item with a potential value of " + value + " pts.";
    }

    /**
     *
     * @return description String of basic item info
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return item type from the api types  index (is further sorted in interactor)
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @return name of item
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return value items preset value that will be adjusted depending on item type
     */
    public int getValue() {return value;}

}

