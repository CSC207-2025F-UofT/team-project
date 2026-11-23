package use_case.meal_plan;

import entity.IngredientName;
import entity.MealPlan;
import entity.MealSlot;
import entity.MealType;
import entity.PlannedMeal;
import entity.RecipeIngredient;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MealPlanWeekOutputDataTest {

    private static final LocalDate MONDAY = LocalDate.of(2026, 11, 16);

    @Test
    void copiesEveryCellInOrder() {
        MealPlanWeekOutputData week = MealPlanWeekOutputData.from(new MealPlan(MONDAY));

        assertEquals(MONDAY, week.getWeekStart());
        assertEquals(21, week.getSlots().size());
        assertEquals(DayOfWeek.MONDAY, week.getSlots().get(0).getDay());
        assertEquals("BREAKFAST", week.getSlots().get(0).getMealType());
        assertEquals(DayOfWeek.SUNDAY, week.getSlots().get(20).getDay());
        assertEquals("DINNER", week.getSlots().get(20).getMealType());
        assertTrue(week.isEmpty());
        for (MealSlotOutputData slot : week.getSlots()) {
            assertFalse(slot.isFilled());
            assertTrue(slot.getIngredients().isEmpty());
        }
    }

    @Test
    void copiesPlannedMealAsPlainData() {
        MealPlan plan = new MealPlan(MONDAY);
        PlannedMeal soup = new PlannedMeal("7", "Tomato Soup", List.of(
                new RecipeIngredient(IngredientName.fromRawName("Tomatoes"), "4 large"),
                new RecipeIngredient(IngredientName.fromRawName("Salt"), null)));
        soup.markCooked();
        plan.assign(new MealSlot(DayOfWeek.WEDNESDAY, MealType.LUNCH), soup);

        MealPlanWeekOutputData week = MealPlanWeekOutputData.from(plan);

        assertFalse(week.isEmpty());
        MealSlotOutputData wednesdayLunch = week.getSlots().get(7);
        assertEquals(DayOfWeek.WEDNESDAY, wednesdayLunch.getDay());
        assertEquals("LUNCH", wednesdayLunch.getMealType());
        assertTrue(wednesdayLunch.isFilled());
        assertTrue(wednesdayLunch.isCooked());
        assertEquals("Tomato Soup", wednesdayLunch.getRecipeName());
        assertEquals(2, wednesdayLunch.getIngredients().size());
        assertEquals("4 large", wednesdayLunch.getIngredients().get(0).getMeasure());
        assertFalse(wednesdayLunch.getIngredients().get(0).getName().isEmpty());
        assertEquals("", wednesdayLunch.getIngredients().get(1).getMeasure());
    }

    @Test
    void laterChangesToThePlanDoNotLeakIntoTheOutput() {
        MealPlan plan = new MealPlan(MONDAY);
        MealSlot fridayDinner = new MealSlot(DayOfWeek.FRIDAY, MealType.DINNER);
        plan.assign(fridayDinner, new PlannedMeal("1", "Pasta", List.of()));

        MealPlanWeekOutputData week = MealPlanWeekOutputData.from(plan);
        plan.remove(fridayDinner);

        assertFalse(week.isEmpty());
        assertEquals("Pasta", week.getSlots().get(14).getRecipeName());
    }

    @Test
    void slotListCannotBeChangedFromOutside() {
        MealPlanWeekOutputData week = MealPlanWeekOutputData.from(new MealPlan(MONDAY));

        assertThrows(UnsupportedOperationException.class, () -> week.getSlots().clear());
    }
}
