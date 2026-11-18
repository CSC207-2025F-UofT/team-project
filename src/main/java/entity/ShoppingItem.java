package entity;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * One line on a shopping list: an ingredient that is missing or running low.
 */
public class ShoppingItem {

    /**
     * Why this ingredient needs to be bought.
     */
    public enum Reason {
        MISSING,
        LOW
    }

    private final IngredientName name;
    private final Reason reason;
    private final Set<String> neededFor;
    private boolean bought;

    /**
     * Creates a shopping item that has not been bought yet.
     *
     * @param name the ingredient name
     * @param reason why it needs to be bought
     * @param neededFor the recipe names that need it
     */
    public ShoppingItem(IngredientName name, Reason reason, Set<String> neededFor) {
        if (name == null || reason == null) {
            throw new IllegalArgumentException("Name and reason cannot be null");
        }
        if (neededFor == null || neededFor.isEmpty()) {
            throw new IllegalArgumentException("neededFor cannot be null or empty");
        }
        this.name = name;
        this.reason = reason;
        this.neededFor = new LinkedHashSet<>(neededFor);
        this.bought = false;
    }

    /**
     * Returns the ingredient name.
     *
     * @return the name
     */
    public IngredientName getName() {
        return name;
    }

    /**
     * Returns why this item is on the list.
     *
     * @return the reason
     */
    public Reason getReason() {
        return reason;
    }

    /**
     * Returns the recipe names that need this ingredient.
     *
     * @return an unmodifiable set of recipe names
     */
    public Set<String> getNeededFor() {
        return Collections.unmodifiableSet(neededFor);
    }

    /**
     * Returns whether the user has already bought this item.
     *
     * @return true if bought
     */
    public boolean isBought() {
        return bought;
    }

    /**
     * Sets the bought flag.
     *
     * @param bought the new value
     */
    public void setBought(boolean bought) {
        this.bought = bought;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ShoppingItem)) {
            return false;
        }
        ShoppingItem that = (ShoppingItem) other;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
