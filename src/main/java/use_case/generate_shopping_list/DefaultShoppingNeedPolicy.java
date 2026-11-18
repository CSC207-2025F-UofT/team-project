package use_case.generate_shopping_list;

import entity.PantryItem;
import entity.ShoppingItem;
import entity.StockLevel;

import java.time.LocalDate;

/**
 * Default shopping rules: missing or expired → MISSING; LOW stock → LOW; otherwise skip.
 */
public class DefaultShoppingNeedPolicy implements ShoppingNeedPolicy {

    @Override
    public ShoppingItem.Reason classify(PantryItem pantryItem, LocalDate today) {
        if (pantryItem == null) {
            return ShoppingItem.Reason.MISSING;
        }
        if (pantryItem.isExpired(today)) {
            return ShoppingItem.Reason.MISSING;
        }
        if (pantryItem.getStockLevel() == StockLevel.LOW) {
            return ShoppingItem.Reason.LOW;
        }
        return null;
    }
}
