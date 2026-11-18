package use_case.generate_shopping_list;

import entity.IngredientName;
import entity.PantryItem;
import entity.ShoppingItem;
import entity.StockLevel;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DefaultShoppingNeedPolicyTest {

    private final DefaultShoppingNeedPolicy policy = new DefaultShoppingNeedPolicy();
    private final LocalDate today = LocalDate.of(2026, 11, 18);

    @Test
    void missingWhenNotInPantry() {
        assertEquals(ShoppingItem.Reason.MISSING, policy.classify(null, today));
    }

    @Test
    void missingWhenExpired() {
        PantryItem item = new PantryItem(
                IngredientName.fromRawName("Milk"), today.minusDays(1), StockLevel.ENOUGH);
        assertEquals(ShoppingItem.Reason.MISSING, policy.classify(item, today));
    }

    @Test
    void lowWhenStockIsLowAndNotExpired() {
        PantryItem item = new PantryItem(
                IngredientName.fromRawName("Milk"), today.plusDays(2), StockLevel.LOW);
        assertEquals(ShoppingItem.Reason.LOW, policy.classify(item, today));
    }

    @Test
    void skipsWhenEnoughAndNotExpired() {
        PantryItem item = new PantryItem(
                IngredientName.fromRawName("Milk"), today.plusDays(2), StockLevel.ENOUGH);
        assertNull(policy.classify(item, today));
    }
}
