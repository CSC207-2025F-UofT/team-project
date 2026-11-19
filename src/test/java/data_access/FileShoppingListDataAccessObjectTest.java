package data_access;

import entity.IngredientName;
import entity.ShoppingItem;
import entity.ShoppingList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileShoppingListDataAccessObjectTest {

    @TempDir
    Path tempDir;

    @Test
    void savesAndLoadsAcrossInstances() {
        Path file = tempDir.resolve("shopping_list.json");
        FileShoppingListDataAccessObject writer = new FileShoppingListDataAccessObject(file);

        LocalDate monday = LocalDate.of(2026, 11, 16);
        ShoppingItem item = new ShoppingItem(
                IngredientName.fromRawName("Garlic"),
                ShoppingItem.Reason.MISSING,
                Set.of("Pasta"));
        item.setBought(true);
        writer.saveShoppingList(new ShoppingList(monday, List.of(item)));

        FileShoppingListDataAccessObject reader = new FileShoppingListDataAccessObject(file);
        ShoppingList loaded = reader.loadShoppingList(monday);

        assertEquals(1, loaded.getItems().size());
        assertEquals("Garlic", loaded.getItems().get(0).getName().getDisplayName());
        assertTrue(loaded.getItems().get(0).isBought());
        assertEquals(Set.of("Pasta"), loaded.getItems().get(0).getNeededFor());
        assertNull(reader.loadShoppingList(monday.plusWeeks(1)));
    }

    @Test
    void overwritesExistingWeek() {
        Path file = tempDir.resolve("shopping_list.json");
        FileShoppingListDataAccessObject dao = new FileShoppingListDataAccessObject(file);
        LocalDate monday = LocalDate.of(2026, 11, 16);

        dao.saveShoppingList(new ShoppingList(monday, List.of(
                new ShoppingItem(IngredientName.fromRawName("Salt"),
                        ShoppingItem.Reason.LOW, Set.of("A")))));
        dao.saveShoppingList(new ShoppingList(monday, List.of(
                new ShoppingItem(IngredientName.fromRawName("Pepper"),
                        ShoppingItem.Reason.MISSING, Set.of("B")))));

        ShoppingList loaded = dao.loadShoppingList(monday);
        assertEquals(1, loaded.getItems().size());
        assertEquals("Pepper", loaded.getItems().get(0).getName().getDisplayName());
    }
}
