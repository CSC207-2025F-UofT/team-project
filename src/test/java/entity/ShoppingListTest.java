package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingListTest {

    private final LocalDate monday = LocalDate.of(2026, 11, 16);

    @Test
    void findsAndTogglesBought() {
        ShoppingItem garlic = new ShoppingItem(
                IngredientName.fromRawName("Garlic"),
                ShoppingItem.Reason.MISSING,
                Set.of("Pasta"));
        ShoppingList list = new ShoppingList(monday, List.of(garlic));

        assertEquals(monday, list.getWeekStart());
        assertEquals(1, list.getItems().size());
        assertEquals(garlic, list.find(IngredientName.fromRawName("garlic")));

        list.toggleBought(IngredientName.fromRawName("Garlic"));
        assertTrue(list.find(IngredientName.fromRawName("Garlic")).isBought());

        list.toggleBought(IngredientName.fromRawName("Garlic"));
        assertFalse(list.find(IngredientName.fromRawName("Garlic")).isBought());
    }

    @Test
    void findReturnsNullWhenMissing() {
        ShoppingList list = new ShoppingList(monday, List.of());
        assertNull(list.find(IngredientName.fromRawName("Salt")));
    }

    @Test
    void toggleBoughtFailsWhenItemMissing() {
        ShoppingList list = new ShoppingList(monday, List.of());
        assertThrows(IllegalArgumentException.class,
                () -> list.toggleBought(IngredientName.fromRawName("Salt")));
    }

    @Test
    void rejectsNullWeekStartOrItems() {
        assertThrows(IllegalArgumentException.class,
                () -> new ShoppingList(null, List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> new ShoppingList(monday, null));
    }
}
