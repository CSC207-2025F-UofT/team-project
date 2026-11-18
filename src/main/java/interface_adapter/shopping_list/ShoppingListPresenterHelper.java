package interface_adapter.shopping_list;

import entity.ShoppingItem;
import entity.ShoppingList;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Shared helpers for shopping-list presenters.
 */
public final class ShoppingListPresenterHelper {

    private ShoppingListPresenterHelper() {
    }

    /**
     * Copies a shopping list into the view state, split into missing and low groups.
     *
     * @param state the view state to update
     * @param shoppingList the list to display, or null
     */
    public static void applyList(ShoppingListState state, ShoppingList shoppingList) {
        List<ShoppingListItemViewData> missing = new ArrayList<>();
        List<ShoppingListItemViewData> low = new ArrayList<>();
        if (shoppingList != null) {
            state.setWeekStart(shoppingList.getWeekStart());
            for (ShoppingItem item : shoppingList.getItems()) {
                ShoppingListItemViewData row = toRow(item);
                if (item.getReason() == ShoppingItem.Reason.MISSING) {
                    missing.add(row);
                }
                else {
                    low.add(row);
                }
            }
        }
        state.setMissingItems(missing);
        state.setLowItems(low);
    }

    private static ShoppingListItemViewData toRow(ShoppingItem item) {
        StringJoiner joiner = new StringJoiner(", ");
        for (String recipeName : item.getNeededFor()) {
            joiner.add(recipeName);
        }
        return new ShoppingListItemViewData(
                item.getName().getDisplayName(),
                item.getReason().name(),
                joiner.toString(),
                item.isBought());
    }
}
