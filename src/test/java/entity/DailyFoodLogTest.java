
package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the DailyFoodLog entity.
 */
class DailyFoodLogTest {

    private static final LocalDate TEST_DATE =
            LocalDate.of(2026, 11, 16);

    @Test
    void testCreateEmptyFoodLog() {
        DailyFoodLog log =
                new DailyFoodLog("log001", TEST_DATE);

        assertEquals("log001", log.getId());
        assertEquals(TEST_DATE, log.getDate());
        assertTrue(log.getRecordedMeals().isEmpty());
    }

    @Test
    void testAddRecordedMeal() {
        DailyFoodLog log = createEmptyLog();

        RecordedMeal breakfast =
                createRecordedMeal(
                        "record001", "breakfast", 8);

        log.addMeal(breakfast);

        assertEquals(1, log.getRecordedMeals().size());

        assertSame(
                breakfast, log.getMealById("record001"));
    }

    @Test
    void testRejectMealFromDifferentDate() {
        DailyFoodLog log = createEmptyLog();

        RecordedMeal wrongDate = new RecordedMeal(
                "record001",
                "lunch",
                LocalDateTime.of(2026, 11, 17, 12, 0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> log.addMeal(wrongDate)
        );
    }

    @Test
    void testRejectDuplicateRecordId() {
        DailyFoodLog log = createEmptyLog();

        log.addMeal(
                createRecordedMeal(
                        "record001", "breakfast", 8));

        assertThrows(
                IllegalArgumentException.class,
                () -> log.addMeal(
                        createRecordedMeal(
                                "record001", "lunch", 12))
        );
    }

    @Test
    void testAllowMultipleMealsInSameSlot() {
        DailyFoodLog log = createEmptyLog();

        log.addMeal(
                createRecordedMeal(
                        "record001", "lunch", 12));

        log.addMeal(
                createRecordedMeal(
                        "record002", "lunch", 13));

        assertEquals(2, log.getRecordedMeals().size());
    }

    @Test
    void testRemoveRecordedMeal() {
        DailyFoodLog log = createEmptyLog();

        log.addMeal(
                createRecordedMeal(
                        "record001", "breakfast", 8));

        assertTrue(log.removeMeal("record001"));
        assertTrue(log.getRecordedMeals().isEmpty());

        assertFalse(log.removeMeal("record001"));
    }

    @Test
    void testCalculateDailyNutrition() {
        DailyFoodLog log = createEmptyLog();

        RecordedMeal breakfast =
                createRecordedMeal(
                        "record001", "breakfast", 8);

        RecordedMeal lunch =
                createRecordedMeal(
                        "record002", "lunch", 12);

        breakfast.addPortion(createRicePortion(100));
        lunch.addPortion(createRicePortion(250));

        log.addMeal(breakfast);
        log.addMeal(lunch);

        assertEquals(
                455,
                log.getTotalNutrition().getCalories(),
                0.001
        );
    }

    @Test
    void testEmptyLogHasZeroNutrition() {
        DailyFoodLog log = createEmptyLog();

        assertEquals(
                0, log.getTotalNutrition().getCalories());
    }

    @Test
    void testConstructWithExistingMeals() {
        RecordedMeal breakfast =
                createRecordedMeal(
                        "record001", "breakfast", 8);

        RecordedMeal lunch =
                createRecordedMeal(
                        "record002", "lunch", 12);

        DailyFoodLog log = new DailyFoodLog(
                "log001",
                TEST_DATE,
                List.of(breakfast, lunch)
        );

        assertEquals(2, log.getRecordedMeals().size());
    }

    @Test
    void testRejectNullMeal() {
        DailyFoodLog log = createEmptyLog();

        assertThrows(
                IllegalArgumentException.class,
                () -> log.addMeal(null)
        );
    }

    @Test
    void testRejectNullDate() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DailyFoodLog("log001", null)
        );
    }

    @Test
    void testRecordedMealsListIsUnmodifiable() {
        DailyFoodLog log = createEmptyLog();

        assertThrows(
                UnsupportedOperationException.class,
                () -> log.getRecordedMeals().clear()
        );
    }

    private DailyFoodLog createEmptyLog() {
        return new DailyFoodLog("log001", TEST_DATE);
    }

    private RecordedMeal createRecordedMeal(
            String id, String slotId, int hour) {

        return new RecordedMeal(
                id,
                slotId,
                TEST_DATE.atTime(hour, 0)
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
