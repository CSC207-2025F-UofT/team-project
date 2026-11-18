package interface_adapter.shopping_list;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Mutable state for the shopping list view.
 */
public class ShoppingListState {

    private LocalDate weekStart;
    private List<ShoppingListItemViewData> missingItems = new ArrayList<>();
    private List<ShoppingListItemViewData> lowItems = new ArrayList<>();
    private String message = "";
    private String error = "";

    /**
     * Returns the week start.
     *
     * @return the week start
     */
    public LocalDate getWeekStart() {
        return weekStart;
    }

    /**
     * Sets the week start.
     *
     * @param weekStart the week start
     */
    public void setWeekStart(LocalDate weekStart) {
        this.weekStart = weekStart;
    }

    /**
     * Returns missing items.
     *
     * @return missing items
     */
    public List<ShoppingListItemViewData> getMissingItems() {
        return missingItems;
    }

    /**
     * Sets missing items.
     *
     * @param missingItems the missing items
     */
    public void setMissingItems(List<ShoppingListItemViewData> missingItems) {
        this.missingItems = new ArrayList<>(missingItems);
    }

    /**
     * Returns low-stock items.
     *
     * @return low items
     */
    public List<ShoppingListItemViewData> getLowItems() {
        return lowItems;
    }

    /**
     * Sets low-stock items.
     *
     * @param lowItems the low items
     */
    public void setLowItems(List<ShoppingListItemViewData> lowItems) {
        this.lowItems = new ArrayList<>(lowItems);
    }

    /**
     * Returns the info message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the info message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message == null ? "" : message;
    }

    /**
     * Returns the error message.
     *
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message.
     *
     * @param error the error
     */
    public void setError(String error) {
        this.error = error == null ? "" : error;
    }
}
