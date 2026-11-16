
package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the DailyMealPlan entity.
 */
class DailyMealPlanTest {

    private static final LocalDate TEST_DATE =
            LocalDate.of(2026, 11, 16);

    @Test
    void testCreateEmptyDailyPlan() {
        DailyMealPlan plan =
                new DailyMealPlan("plan001", TEST_DATE);

        assertEquals("plan001", plan.getId());
        assertEquals(TEST_DATE, plan.getDate());
        assertTrue(plan.getPlannedMeals().isEmpty());
    }

    @Test
    void testAddPlannedMeal() {
        DailyMealPlan plan = createEmptyPlan();

        PlannedMeal breakfast =
                new PlannedMeal("meal001", "breakfast");

        plan.addMeal(breakfast);

        assertEquals(1, plan.getPlannedMeals().size());
        assertSame(breakfast, plan.getMealById("meal001"));
    }

    @Test
    void testGetMealBySlotId() {
        DailyMealPlan plan = createEmptyPlan();

        plan.addMeal(
                new PlannedMeal("meal001", "breakfast"));

        PlannedMeal found =
                plan.getMealBySlotId("breakfast");

        assertNotNull(found);
        assertEquals("meal001", found.getId());
    }

    @Test
    void testRejectDuplicateMealId() {
        DailyMealPlan plan = createEmptyPlan();

        plan.addMeal(
                new PlannedMeal("meal001", "breakfast"));

        assertThrows(
                IllegalArgumentException.class,
                () -> plan.addMeal(
                        new PlannedMeal("meal001", "lunch"))
        );
    }

    @Test
    void testRejectDuplicateMealSlot() {
        DailyMealPlan plan = createEmptyPlan();

        plan.addMeal(
                new PlannedMeal("meal001", "breakfast"));

        assertThrows(
                IllegalArgumentException.class,
                () -> plan.addMeal(
                        new PlannedMeal("meal002", "breakfast"))
        );
    }

    @Test
    void testAllowDifferentSlots() {
        DailyMealPlan plan = createEmptyPlan();

        plan.addMeal(
                new PlannedMeal("meal001", "breakfast"));

        plan.addMeal(
                new PlannedMeal("meal002", "lunch"));

        assertEquals(2, plan.getPlannedMeals().size());
    }

    @Test
    void testRemovePlannedMeal() {
        DailyMealPlan plan = createEmptyPlan();

        plan.addMeal(
                new PlannedMeal("meal001", "breakfast"));

        assertTrue(plan.removeMeal("meal001"));
        assertNull(plan.getMealById("meal001"));
        assertFalse(plan.removeMeal("meal001"));
    }

    @Test
    void testReplacePlannedMeal() {
        DailyMealPlan plan = createEmptyPlan();

        plan.addMeal(
                new PlannedMeal("meal001", "lunch"));

        PlannedMeal updated =
                new PlannedMeal("meal001", "lunch");

        updated.addPortion(createRicePortion(200));

        assertTrue(plan.replaceMeal(updated));

        assertEquals(
                260, plan.getTotalNutrition().getCalories());
    }

    @Test
    void testRejectReplacementWithDifferentSlot() {
        DailyMealPlan plan = createEmptyPlan();

        plan.addMeal(
                new PlannedMeal("meal001", "lunch"));

        PlannedMeal updated =
                new PlannedMeal("meal001", "dinner");

        assertThrows(
                IllegalArgumentException.class,
                () -> plan.replaceMeal(updated)
        );
    }

    @Test
    void testCalculateDailyNutrition() {
        DailyMealPlan plan = createEmptyPlan();

        PlannedMeal breakfast =
                new PlannedMeal("meal001", "breakfast");

        PlannedMeal lunch =
                new PlannedMeal("meal002", "lunch");

        breakfast.addPortion(createRicePortion(100));
        lunch.addPortion(createRicePortion(200));

        plan.addMeal(breakfast);
        plan.addMeal(lunch);

        assertEquals(
                390,
                plan.getTotalNutrition().getCalories(),
                0.001
        );
    }

    @Test
    void testCreateWithExistingMeals() {
        DailyMealPlan plan = new DailyMealPlan(
                "plan001",
                TEST_DATE,
                List.of(
                        new PlannedMeal("m1", "breakfast"),
                        new PlannedMeal("m2", "lunch")
                )
        );

        assertEquals(2, plan.getPlannedMeals().size());
    }

    @Test
    void testRejectNullMeal() {
        DailyMealPlan plan = createEmptyPlan();

        assertThrows(
                IllegalArgumentException.class,
                () -> plan.addMeal(null)
        );
    }

    @Test
    void testRejectNullDate() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DailyMealPlan("plan001", null)
        );
    }

    @Test
    void testPlannedMealsListIsUnmodifiable() {
        DailyMealPlan plan = createEmptyPlan();

        assertThrows(
                UnsupportedOperationException.class,
                () -> plan.getPlannedMeals().clear()
        );
    }

    private DailyMealPlan createEmptyPlan() {
        return new DailyMealPlan("plan001", TEST_DATE);
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
