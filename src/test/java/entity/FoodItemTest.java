package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the FoodItem entity.
 */
class FoodItemTest {

    @Test
    void testConstructorAndGetters() {
        NutritionTotals nutrition =
                new NutritionTotals(130, 2.7, 28.2, 0.3);

        FoodItem food = new FoodItem(
                "food001", "Cooked Rice", nutrition);

        assertEquals("food001", food.getId());
        assertEquals("Cooked Rice", food.getName());
        assertSame(nutrition, food.getNutritionPer100g());
    }

    @Test
    void testCalculateNutritionFor100Grams() {
        FoodItem rice = createRice();

        NutritionTotals result =
                rice.calculateNutrition(100);

        assertEquals(130, result.getCalories());
        assertEquals(2.7, result.getProtein());
        assertEquals(28.2, result.getCarbs());
        assertEquals(0.3, result.getFat());
    }

    @Test
    void testCalculateNutritionFor250Grams() {
        FoodItem rice = createRice();

        NutritionTotals result =
                rice.calculateNutrition(250);

        assertEquals(325, result.getCalories());
        assertEquals(6.75, result.getProtein());
        assertEquals(70.5, result.getCarbs());
        assertEquals(0.75, result.getFat());
    }

    @Test
    void testRejectInvalidId() {
        NutritionTotals nutrition =
                new NutritionTotals(100, 5, 20, 2);

        assertThrows(
                IllegalArgumentException.class,
                () -> new FoodItem(null, "Rice", nutrition)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new FoodItem(" ", "Rice", nutrition)
        );
    }

    @Test
    void testRejectInvalidName() {
        NutritionTotals nutrition =
                new NutritionTotals(100, 5, 20, 2);

        assertThrows(
                IllegalArgumentException.class,
                () -> new FoodItem("food001", "", nutrition)
        );
    }

    @Test
    void testRejectNullNutrition() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new FoodItem("food001", "Rice", null)
        );
    }

    @Test
    void testRejectInvalidQuantity() {
        FoodItem rice = createRice();

        assertThrows(
                IllegalArgumentException.class,
                () -> rice.calculateNutrition(0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> rice.calculateNutrition(-100)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> rice.calculateNutrition(Double.NaN)
        );
    }

    private FoodItem createRice() {
        return new FoodItem(
                "food001",
                "Cooked Rice",
                new NutritionTotals(130, 2.7, 28.2, 0.3)
        );
    }
}
