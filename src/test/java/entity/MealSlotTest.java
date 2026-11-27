
package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MealSlot entity.
 */
class MealSlotTest {

    @Test
    void testConstructorAndGetters() {
        MealSlot slot =
                new MealSlot("slot001", "Breakfast");

        assertEquals("slot001", slot.getId());
        assertEquals("Breakfast", slot.getName());
    }

    @Test
    void testCustomMealSlot() {
        MealSlot slot =
                new MealSlot("slot002", "Pre-Workout Snack");

        assertEquals("Pre-Workout Snack", slot.getName());
    }

    @Test
    void testRejectNullId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MealSlot(null, "Breakfast")
        );
    }

    @Test
    void testRejectBlankId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MealSlot("  ", "Breakfast")
        );
    }

    @Test
    void testRejectNullName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MealSlot("slot001", null)
        );
    }

    @Test
    void testRejectBlankName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MealSlot("slot001", "  ")
        );
    }
}
