package entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MealSchedule entity.
 */
class MealScheduleTest {

    @Test
    void testEmptySchedule() {
        MealSchedule schedule = new MealSchedule();

        assertTrue(schedule.getSlots().isEmpty());
    }

    @Test
    void testConstructWithExistingSlots() {
        MealSchedule schedule = new MealSchedule(
                List.of(
                        new MealSlot("s1", "Breakfast"),
                        new MealSlot("s2", "Lunch")
                )
        );

        assertEquals(2, schedule.getSlots().size());
        assertEquals("Breakfast",
                schedule.getSlots().get(0).getName());
    }

    @Test
    void testAddSlot() {
        MealSchedule schedule = new MealSchedule();

        schedule.addSlot(new MealSlot("s1", "Breakfast"));

        assertEquals(1, schedule.getSlots().size());
        assertNotNull(schedule.getSlotById("s1"));
    }

    @Test
    void testRejectDuplicateSlotId() {
        MealSchedule schedule = new MealSchedule();

        schedule.addSlot(new MealSlot("s1", "Breakfast"));

        assertThrows(
                IllegalArgumentException.class,
                () -> schedule.addSlot(
                        new MealSlot("s1", "Lunch"))
        );
    }

    @Test
    void testAllowDuplicateNames() {
        MealSchedule schedule = new MealSchedule();

        schedule.addSlot(new MealSlot("s1", "Snack"));
        schedule.addSlot(new MealSlot("s2", "Snack"));

        assertEquals(2, schedule.getSlots().size());
    }

    @Test
    void testRejectNullSlot() {
        MealSchedule schedule = new MealSchedule();

        assertThrows(
                IllegalArgumentException.class,
                () -> schedule.addSlot(null)
        );
    }

    @Test
    void testRenameSlotPreservesId() {
        MealSchedule schedule = new MealSchedule();

        schedule.addSlot(
                new MealSlot("s1", "Morning Snack"));

        assertTrue(schedule.renameSlot(
                "s1", "Pre-Workout Meal"));

        MealSlot renamed = schedule.getSlotById("s1");

        assertEquals("s1", renamed.getId());
        assertEquals("Pre-Workout Meal", renamed.getName());
    }

    @Test
    void testRenameNonexistentSlot() {
        MealSchedule schedule = new MealSchedule();

        assertFalse(schedule.renameSlot(
                "missing", "Snack"));
    }

    @Test
    void testRemoveSlot() {
        MealSchedule schedule = new MealSchedule();

        schedule.addSlot(new MealSlot("s1", "Breakfast"));

        assertTrue(schedule.removeSlot("s1"));
        assertNull(schedule.getSlotById("s1"));
        assertFalse(schedule.removeSlot("s1"));
    }

    @Test
    void testMoveSlot() {
        MealSchedule schedule = new MealSchedule();

        schedule.addSlot(new MealSlot("s1", "Breakfast"));
        schedule.addSlot(new MealSlot("s2", "Lunch"));
        schedule.addSlot(new MealSlot("s3", "Dinner"));

        assertTrue(schedule.moveSlot("s3", 0));

        assertEquals("Dinner",
                schedule.getSlots().get(0).getName());

        assertEquals("Breakfast",
                schedule.getSlots().get(1).getName());

        assertEquals("Lunch",
                schedule.getSlots().get(2).getName());
    }

    @Test
    void testRejectInvalidMoveIndex() {
        MealSchedule schedule = new MealSchedule();

        schedule.addSlot(new MealSlot("s1", "Breakfast"));

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> schedule.moveSlot("s1", 5)
        );
    }

    @Test
    void testSlotsListCannotBeModifiedExternally() {
        MealSchedule schedule = new MealSchedule();

        schedule.addSlot(new MealSlot("s1", "Breakfast"));

        assertThrows(
                UnsupportedOperationException.class,
                () -> schedule.getSlots().clear()
        );
    }

    @Test
    void testRejectDuplicateIdsInConstructor() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MealSchedule(
                        List.of(
                                new MealSlot("s1", "Breakfast"),
                                new MealSlot("s1", "Lunch")
                        )
                )
        );
    }
}
