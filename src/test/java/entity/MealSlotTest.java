package entity;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MealSlotTest {

    @Test
    void sameDayAndMealAreEqualWithSameHash() {
        MealSlot a = new MealSlot(DayOfWeek.MONDAY, MealType.LUNCH);
        MealSlot b = new MealSlot(DayOfWeek.MONDAY, MealType.LUNCH);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void differentDayOrMealAreNotEqual() {
        MealSlot mondayLunch = new MealSlot(DayOfWeek.MONDAY, MealType.LUNCH);
        assertNotEquals(mondayLunch, new MealSlot(DayOfWeek.TUESDAY, MealType.LUNCH));
        assertNotEquals(mondayLunch, new MealSlot(DayOfWeek.MONDAY, MealType.DINNER));
    }

    @Test
    void worksAsMapKey() {
        Map<MealSlot, String> plan = new HashMap<>();
        plan.put(new MealSlot(DayOfWeek.FRIDAY, MealType.DINNER), "Pasta");
        assertEquals("Pasta", plan.get(new MealSlot(DayOfWeek.FRIDAY, MealType.DINNER)));
    }

    @Test
    void rejectsNulls() {
        assertThrows(IllegalArgumentException.class, () -> new MealSlot(null, MealType.LUNCH));
        assertThrows(IllegalArgumentException.class, () -> new MealSlot(DayOfWeek.MONDAY, null));
    }

    @Test
    void ordersByDayThenMeal() {
        MealSlot mondayDinner = new MealSlot(DayOfWeek.MONDAY, MealType.DINNER);
        MealSlot tuesdayBreakfast = new MealSlot(DayOfWeek.TUESDAY, MealType.BREAKFAST);
        MealSlot mondayBreakfast = new MealSlot(DayOfWeek.MONDAY, MealType.BREAKFAST);

        List<MealSlot> slots = new ArrayList<>(List.of(tuesdayBreakfast, mondayDinner, mondayBreakfast));
        Collections.sort(slots);

        assertEquals(List.of(mondayBreakfast, mondayDinner, tuesdayBreakfast), slots);
    }

    @Test
    void allSlotsHasTwentyOneInOrder() {
        List<MealSlot> all = MealSlot.allSlots();
        assertEquals(21, all.size());
        assertEquals(new MealSlot(DayOfWeek.MONDAY, MealType.BREAKFAST), all.get(0));
        assertEquals(new MealSlot(DayOfWeek.SUNDAY, MealType.DINNER), all.get(20));
        assertThrows(UnsupportedOperationException.class, () -> all.add(all.get(0)));
    }
}
