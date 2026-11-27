package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the FoodPortion entity.
 */
class FoodPortionTest {

    @Test
    void testConstructorAndGetters() {
        NutritionTotals nutrition =
                new NutritionTotals(260, 5.4, 56.4, 0.6);

        FoodPortion portion = new FoodPortion(
                "food001", "Rice", 200, nutrition);

        assertEquals("food001", portion.getFoodId());
        assertEquals("Rice", portion.getFoodName());
        assertEquals(200, portion.getQuantityGrams());
        assertSame(nutrition, portion.getNutrition());
    }

    @Test
    void testCreatePortionFromFoodItem() {
        FoodItem rice = createRice();

        FoodPortion portion =
                FoodPortion.from(rice, 250);

        assertEquals("food001", portion.getFoodId());
        assertEquals("Rice", portion.getFoodName());
        assertEquals(250, portion.getQuantityGrams());

        assertEquals(
                325, portion.getNutrition().getCalories());

        assertEquals(
                6.75, portion.getNutrition().getProtein());
    }

    @Test
    void testNutritionSnapshot() {
        FoodItem rice = createRice();

        FoodPortion portion =
                FoodPortion.from(rice, 200);

        // Creating a new FoodItem with the same ID and
        // different nutrition does not modify this portion.
        FoodItem updatedRice = new FoodItem(
                "food001",
                "Updated Rice",
                new NutritionTotals(200, 5, 40, 2)
        );

        assertEquals("Rice", portion.getFoodName());
        assertEquals(
                260, portion.getNutrition().getCalories());

        assertEquals(
                200, updatedRice.getNutritionPer100g()
                        .getCalories());
    }

    @Test
    void testRejectInvalidQuantity() {
        NutritionTotals nutrition =
                new NutritionTotals(100, 5, 20, 2);

        assertThrows(
                IllegalArgumentException.class,
                () -> new FoodPortion(
                        "food001", "Rice", 0, nutrition)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new FoodPortion(
                        "food001", "Rice", -50, nutrition)
        );
    }

    @Test
    void testRejectNullNutrition() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new FoodPortion(
                        "food001", "Rice", 100, null)
        );
    }

    @Test
    void testRejectNullFoodItem() {
        assertThrows(
                IllegalArgumentException.class,
                () -> FoodPortion.from(null, 100)
        );
    }

    @Test
    void testRejectInvalidFoodName() {
        NutritionTotals nutrition =
                new NutritionTotals(100, 5, 20, 2);

        assertThrows(
                IllegalArgumentException.class,
                () -> new FoodPortion(
                        "food001", "", 100, nutrition)
        );
    }

    private FoodItem createRice() {
        return new FoodItem(
                "food001",
                "Rice",
                new NutritionTotals(130, 2.7, 28.2, 0.3)
        );
    }
}
