package entity;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingItemTest {

    @Test
    void createsUnboughtItemWithNeededForRecipes() {
        ShoppingItem item = new ShoppingItem(
                IngredientName.fromRawName("Garlic"),
                ShoppingItem.Reason.MISSING,
                Set.of("Pasta", "Soup"));
        assertEquals("Garlic", item.getName().getDisplayName());
        assertEquals(ShoppingItem.Reason.MISSING, item.getReason());
        assertEquals(Set.of("Pasta", "Soup"), item.getNeededFor());
        assertFalse(item.isBought());
    }

    @Test
    void togglesBoughtFlag() {
        ShoppingItem item = new ShoppingItem(
                IngredientName.fromRawName("Onion"),
                ShoppingItem.Reason.LOW,
                Set.of("Stew"));
        item.setBought(true);
        assertTrue(item.isBought());
        item.setBought(false);
        assertFalse(item.isBought());
    }

    @Test
    void equalsByIngredientNameOnly() {
        ShoppingItem a = new ShoppingItem(
                IngredientName.fromRawName("Salt"),
                ShoppingItem.Reason.MISSING,
                Set.of("A"));
        ShoppingItem b = new ShoppingItem(
                IngredientName.fromRawName("salt"),
                ShoppingItem.Reason.LOW,
                Set.of("B"));
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertTrue(a.equals(a));
        assertNotEquals(a, new ShoppingItem(
                IngredientName.fromRawName("Pepper"),
                ShoppingItem.Reason.MISSING,
                Set.of("A")));
        assertNotEquals(a, "Salt");
    }

    @Test
    void rejectsInvalidConstruction() {
        IngredientName name = IngredientName.fromRawName("Milk");
        assertThrows(IllegalArgumentException.class,
                () -> new ShoppingItem(null, ShoppingItem.Reason.MISSING, Set.of("A")));
        assertThrows(IllegalArgumentException.class,
                () -> new ShoppingItem(name, null, Set.of("A")));
        assertThrows(IllegalArgumentException.class,
                () -> new ShoppingItem(name, ShoppingItem.Reason.MISSING, null));
        assertThrows(IllegalArgumentException.class,
                () -> new ShoppingItem(name, ShoppingItem.Reason.MISSING, Set.of()));
    }
}
