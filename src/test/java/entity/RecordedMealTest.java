package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the RecordedMeal entity.
 */
class RecordedMealTest {

    @Test
    void testConstructorAndGetters() {
        LocalDateTime time =
                LocalDateTime.of(2026, 11, 16, 12, 30);

        RecordedMeal meal = new RecordedMeal(
                "record001", "lunch", time);

        assertEquals("record001", meal.getId());
        assertEquals("lunch", meal.getMealSlotId());
        assertEquals(time, meal.getConsumedAt());
    }

    @Test
    void testCreateWithInitialPortions() {
        RecordedMeal meal = new RecordedMeal(
                "record001",
                "lunch",
                LocalDateTime.of(2026, 11, 16, 12, 30),
                List.of(createRicePortion(250))
        );

        assertEquals(1, meal.getPortions().size());

        assertEquals(
                325, meal.getTotalNutrition().getCalories());
    }

    @Test
    void testAddRecordedFood() {
        RecordedMeal meal = createEmptyRecordedMeal();

        meal.addPortion(createRicePortion(200));

        assertEquals(1, meal.getPortions().size());
        assertEquals(
                260, meal.getTotalNutrition().getCalories());
    }

    @Test
    void testCorrectRecordedQuantity() {
        RecordedMeal meal = createEmptyRecordedMeal();

        meal.addPortion(createRicePortion(100));

        // Correct the portion from 100g to 250g.
        meal.replacePortion(0, createRicePortion(250));

        assertEquals(
                325, meal.getTotalNutrition().getCalories());
    }

    @Test
    void testRemoveRecordedFood() {
        RecordedMeal meal = createEmptyRecordedMeal();

        meal.addPortion(createRicePortion(100));
        meal.removePortion(0);

        assertTrue(meal.getPortions().isEmpty());

        assertEquals(
                0, meal.getTotalNutrition().getCalories());
    }

    @Test
    void testRejectNullConsumptionTime() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new RecordedMeal(
                        "record001", "lunch", null)
        );
    }

    @Test
    void testRejectInvalidId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new RecordedMeal(
                        "", "lunch",
                        LocalDateTime.of(2026, 11, 16, 12, 30))
        );
    }

    @Test
    void testRejectNullInitialPortions() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new RecordedMeal(
                        "record001",
                        "lunch",
                        LocalDateTime.of(2026, 11, 16, 12, 30),
                        null)
        );
    }

    @Test
    void testRecordedMealIsAMeal() {
        RecordedMeal meal = createEmptyRecordedMeal();

        assertInstanceOf(Meal.class, meal);
    }

    private RecordedMeal createEmptyRecordedMeal() {
        return new RecordedMeal(
                "record001",
                "lunch",
                LocalDateTime.of(2026, 11, 16, 12, 30)
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
