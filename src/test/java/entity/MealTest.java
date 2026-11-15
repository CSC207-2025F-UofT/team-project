package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for shared functionality of the abstract Meal class.
 */
class MealTest {

    @Test
    void testPlannedMealInheritance() {
        Meal meal = new PlannedMeal("meal001", "lunch");

        assertInstanceOf(PlannedMeal.class, meal);
        assertEquals("meal001", meal.getId());
        assertEquals("lunch", meal.getMealSlotId());
    }

    @Test
    void testRecordedMealInheritance() {
        Meal meal = new RecordedMeal(
                "record001",
                "dinner",
                LocalDateTime.of(2026, 11, 16, 19, 0)
        );

        assertInstanceOf(RecordedMeal.class, meal);
        assertEquals("record001", meal.getId());
    }

    @Test
    void testAddPortion() {
        Meal meal = new PlannedMeal("meal001", "lunch");

        meal.addPortion(createPortion(100));

        assertEquals(1, meal.getPortions().size());
    }

    @Test
    void testRemovePortion() {
        Meal meal = new PlannedMeal("meal001", "lunch");

        FoodPortion portion = createPortion(100);
        meal.addPortion(portion);

        FoodPortion removed = meal.removePortion(0);

        assertSame(portion, removed);
        assertTrue(meal.getPortions().isEmpty());
    }

    @Test
    void testReplacePortion() {
        Meal meal = new PlannedMeal("meal001", "lunch");

        meal.addPortion(createPortion(100));
        meal.replacePortion(0, createPortion(250));

        assertEquals(
                250,
                meal.getPortions().get(0).getQuantityGrams()
        );
    }

    @Test
    void testCalculateTotalNutrition() {
        Meal meal = new PlannedMeal("meal001", "lunch");

        meal.addPortion(createPortion(100));
        meal.addPortion(createPortion(200));

        // Rice contains 130 calories per 100g.
        assertEquals(
                390,
                meal.getTotalNutrition().getCalories(),
                0.001
        );
    }

    @Test
    void testEmptyMealHasZeroNutrition() {
        Meal meal = new PlannedMeal("meal001", "lunch");

        assertEquals(
                0, meal.getTotalNutrition().getCalories());
    }

    @Test
    void testRejectNullPortion() {
        Meal meal = new PlannedMeal("meal001", "lunch");

        assertThrows(
                IllegalArgumentException.class,
                () -> meal.addPortion(null)
        );
    }

    @Test
    void testRejectNullReplacement() {
        Meal meal = new PlannedMeal("meal001", "lunch");

        meal.addPortion(createPortion(100));

        assertThrows(
                IllegalArgumentException.class,
                () -> meal.replacePortion(0, null)
        );
    }

    @Test
    void testPortionListIsUnmodifiable() {
        Meal meal = new PlannedMeal("meal001", "lunch");

        assertThrows(
                UnsupportedOperationException.class,
                () -> meal.getPortions().add(createPortion(100))
        );
    }

    @Test
    void testConstructorCopiesInitialList() {
        FoodPortion portion = createPortion(100);

        Meal meal = new PlannedMeal(
                "meal001",
                "lunch",
                List.of(portion)
        );

        assertEquals(1, meal.getPortions().size());
        assertSame(portion, meal.getPortions().get(0));
    }

    private FoodPortion createPortion(double grams) {
        FoodItem rice = new FoodItem(
                "food001",
                "Rice",
                new NutritionTotals(130, 2.7, 28.2, 0.3)
        );

        return FoodPortion.from(rice, grams);
    }
}
