package entity;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MealPlanTest {

    private final LocalDate monday = LocalDate.of(2026, 11, 16);

    @Test
    void assignsRemovesMovesAndListsUncooked() {
        MealPlan plan = new MealPlan(monday);
        MealSlot dinner = new MealSlot(DayOfWeek.MONDAY, MealType.DINNER);
        MealSlot lunch = new MealSlot(DayOfWeek.TUESDAY, MealType.LUNCH);
        PlannedMeal pasta = new PlannedMeal("1", "Pasta", List.of(
                new RecipeIngredient(IngredientName.fromRawName("Garlic"), "1")));

        plan.assign(dinner, pasta);
        assertEquals(monday, plan.getWeekStart());
        assertEquals(1, plan.getMeals().size());
        assertEquals(1, plan.uncookedMeals().size());

        plan.move(dinner, lunch);
        assertFalse(plan.getMeals().containsKey(dinner));
        assertTrue(plan.getMeals().containsKey(lunch));

        plan.remove(lunch);
        assertTrue(plan.uncookedMeals().isEmpty());
    }

    @Test
    void rejectsInvalidMovesAndAssignments() {
        MealPlan plan = new MealPlan(monday);
        MealSlot dinner = new MealSlot(DayOfWeek.MONDAY, MealType.DINNER);
        MealSlot lunch = new MealSlot(DayOfWeek.TUESDAY, MealType.LUNCH);
        PlannedMeal pasta = new PlannedMeal("1", "Pasta", List.of());
        PlannedMeal soup = new PlannedMeal("2", "Soup", List.of());

        assertThrows(IllegalArgumentException.class, () -> new MealPlan(null));
        assertThrows(IllegalArgumentException.class, () -> plan.assign(null, pasta));
        assertThrows(IllegalArgumentException.class, () -> plan.assign(dinner, null));

        plan.assign(dinner, pasta);
        assertThrows(IllegalStateException.class, () -> plan.assign(dinner, soup));

        plan.assign(lunch, soup);
        assertThrows(IllegalStateException.class, () -> plan.move(dinner, lunch));

        pasta.markCooked();
        assertThrows(IllegalStateException.class, () -> plan.move(dinner,
                new MealSlot(DayOfWeek.WEDNESDAY, MealType.BREAKFAST)));
        assertThrows(IllegalStateException.class, () -> plan.move(
                new MealSlot(DayOfWeek.FRIDAY, MealType.BREAKFAST), lunch));
        assertThrows(IllegalArgumentException.class, () -> plan.move(null, lunch));
        assertThrows(IllegalArgumentException.class, () -> plan.move(dinner, null));
        assertEquals(1, plan.uncookedMeals().size());
    }

    @Test
    void rejectsNonMondayWeekStart() {
        LocalDate tuesday = LocalDate.of(2026, 11, 17);
        LocalDate sunday = LocalDate.of(2026, 11, 15);
        assertEquals(DayOfWeek.TUESDAY, tuesday.getDayOfWeek());
        assertEquals(DayOfWeek.SUNDAY, sunday.getDayOfWeek());

        assertThrows(IllegalArgumentException.class, () -> new MealPlan(tuesday));
        assertThrows(IllegalArgumentException.class, () -> new MealPlan(sunday));
        assertEquals(monday, new MealPlan(monday).getWeekStart());
    }

    @Test
    void removeRejectsNullAndIgnoresEmptySlot() {
        MealPlan plan = new MealPlan(monday);
        MealSlot empty = new MealSlot(DayOfWeek.WEDNESDAY, MealType.BREAKFAST);

        assertThrows(IllegalArgumentException.class, () -> plan.remove(null));
        plan.remove(empty);
        assertTrue(plan.getMeals().isEmpty());
    }
}
