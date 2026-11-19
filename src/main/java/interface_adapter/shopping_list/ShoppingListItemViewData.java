package interface_adapter.shopping_list;

/**
 * Flat view data for one shopping-list row.
 */
public class ShoppingListItemViewData {

    private final String name;
    private final String reason;
    private final String neededFor;
    private final boolean bought;

    /**
     * Creates row data.
     *
     * @param name the ingredient display name
     * @param reason MISSING or LOW
     * @param neededFor comma-separated recipe names
     * @param bought whether the item is bought
     */
    public ShoppingListItemViewData(String name, String reason, String neededFor, boolean bought) {
        this.name = name;
        this.reason = reason;
        this.neededFor = neededFor;
        this.bought = bought;
    }

    /**
     * Returns the ingredient name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the reason code.
     *
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Returns the recipe names text.
     *
     * @return the needed-for text
     */
    public String getNeededFor() {
        return neededFor;
    }

    /**
     * Returns whether the item is bought.
     *
     * @return true if bought
     */
    public boolean isBought() {
        return bought;
    }
}
