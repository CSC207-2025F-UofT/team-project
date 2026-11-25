package data_access;

import entity.Category;
import entity.ClothingItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests that ClosetData saves items to the JSON file and loads them back
 * (the "close the app and reopen it" check).
 */
class ClosetDataTest {

    @TempDir
    Path temp;

    private static ClothingItem shirt(String id) {
        return new ClothingItem(id, "Pink Dress Shirt", Category.TOP, "Long-sleeved shirt",
                "Pink", "Feminine", false, 2, "data/images/ginni/" + id + ".png");
    }

    @Test
    void itemsSurviveARestart() {
        final String file = temp.resolve("closet.json").toString();
        final ClosetData first = new ClosetData(file);
        first.saveItem("ginni", shirt("c001"));

        final ClosetData reopened = new ClosetData(file);
        final List<ClothingItem> items = reopened.getAllItems("ginni");

        assertEquals(1, items.size());
        final ClothingItem item = items.get(0);
        assertEquals("c001", item.getId());
        assertEquals(Category.TOP, item.getCategory());
        assertEquals("Pink", item.getColor());
        assertEquals(2, item.getWeatherRating());
        assertEquals("data/images/ginni/c001.png", item.getImagePath());
    }

    @Test
    void eachUserHasTheirOwnCloset() {
        final ClosetData data = new ClosetData(temp.resolve("closet.json").toString());
        data.saveItem("ginni", shirt("c001"));
        assertTrue(data.getAllItems("rie").isEmpty());
    }

    @Test
    void idsCountUpAndSkipUsedOnes() {
        final ClosetData data = new ClosetData(temp.resolve("closet.json").toString());
        assertEquals("c001", data.generateItemId("ginni"));
        data.saveItem("ginni", shirt("c001"));
        data.saveItem("ginni", shirt("c007"));
        assertEquals("c008", data.generateItemId("ginni"));
    }

    @Test
    void findsItemById() {
        final ClosetData data = new ClosetData(temp.resolve("closet.json").toString());
        data.saveItem("ginni", shirt("c001"));
        assertEquals("Pink Dress Shirt", data.getItemById("ginni", "c001").getName());
        assertNull(data.getItemById("ginni", "c999"));
    }

    @Test
    void noFileMeansEmptyCloset() {
        final ClosetData data = new ClosetData(temp.resolve("missing.json").toString());
        assertTrue(data.getAllItems("ginni").isEmpty());
    }
}
