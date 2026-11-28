package entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PlannedMeal entity.
 */
class PlannedMealTest {

    @Test
    void testCreateEmptyPlannedMeal() {
        PlannedMeal meal =
                new PlannedMeal("meal001", "breakfast");

        assertEquals("meal001", meal.getId());
        assertEquals("breakfast", meal.getMealSlotId());
        assertTrue(meal.getPortions().isEmpty());
    }

    @Test
    void testCreatePlannedMealWithPortions() {
        FoodPortion portion = createRicePortion(200);

        PlannedMeal meal = new PlannedMeal(
                "meal001",
                "lunch",
                List.of(portion)
        );

        assertEquals(1, meal.getPortions().size());

        assertEquals(
                260, meal.getTotalNutrition().getCalories());
    }

    @Test
    void testAddMultipleFoodPortions() {
        PlannedMeal meal =
                new PlannedMeal("meal001", "lunch");

        meal.addPortion(createRicePortion(200));
        meal.addPortion(createRicePortion(100));

        assertEquals(2, meal.getPortions().size());

        assertEquals(
                390, meal.getTotalNutrition().getCalories());
    }

    @Test
    void testReplacePlannedPortion() {
        PlannedMeal meal =
                new PlannedMeal("meal001", "lunch");

        meal.addPortion(createRicePortion(100));
        meal.replacePortion(0, createRicePortion(250));

        assertEquals(
                325, meal.getTotalNutrition().getCalories());
    }

    @Test
    void testRejectInvalidId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedMeal("", "lunch")
        );
    }

    @Test
    void testRejectInvalidSlotId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedMeal("meal001", null)
        );
    }

    @Test
    void testRejectNullInitialPortions() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedMeal(
                        "meal001", "lunch", null)
        );
    }

    @Test
    void testRejectNullPortionInConstructor() {
        // Arrays.asList permits null elements.
        List<FoodPortion> portions =
                java.util.Arrays.asList(
                        createRicePortion(100), null);

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedMeal(
                        "meal001", "lunch", portions)
        );
    }

    private FoodPortion createRicePortion(double grams) {
        FoodItem rice = new FoodItem(
                "food001",
                "Rice",
                new NutritionTotals(130, 2.7, 28.2, 0.3)
        );

        return FoodPortion.from(rice, grams);
    }
}
