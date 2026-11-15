package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an ordered collection of meal slots in a daily schedule.
 *
 * Users can customize their schedule by adding, removing, renaming,
 * and reordering meal slots.
 *
 * This class does not store food items or recorded meals.
 */
public class MealSchedule {

    // Ordered collection of meal slots.
    private final List<MealSlot> slots;

    /**
     * Constructs an empty meal schedule.
     */
    public MealSchedule() {
        this.slots = new ArrayList<>();
    }

    /**
     * Constructs a schedule with an existing list of meal slots.
     *
     * @param initialSlots meal slots to include
     * @throws IllegalArgumentException if the list contains
     *                                  null slots or duplicate IDs
     */
    public MealSchedule(List<MealSlot> initialSlots) {
        this();

        if (initialSlots == null) {
            throw new IllegalArgumentException(
                    "Initial slots cannot be null.");
        }

        // Reuse addSlot so the same validation rules apply.
        for (MealSlot slot : initialSlots) {
            addSlot(slot);
        }
    }

    /**
     * Returns the meal slots in their current order.
     *
     * The returned list cannot be modified by callers.
     *
     * @return unmodifiable list of meal slots
     */
    public List<MealSlot> getSlots() {
        return Collections.unmodifiableList(slots);
    }

    /**
     * Finds a meal slot by its unique identifier.
     *
     * @param id ID of the slot
     * @return matching MealSlot, or null if not found
     */
    public MealSlot getSlotById(String id) {
        for (MealSlot slot : slots) {
            if (slot.getId().equals(id)) {
                return slot;
            }
        }
        return null;
    }

    /**
     * Adds a meal slot to the end of the schedule.
     *
     * @param newSlot slot to add
     * @throws IllegalArgumentException if the slot is null
     *                                  or its ID already exists
     */
    public void addSlot(MealSlot newSlot) {
        if (newSlot == null) {
            throw new IllegalArgumentException(
                    "Meal slot cannot be null.");
        }

        // A schedule cannot contain two slots with the same ID.
        if (getSlotById(newSlot.getId()) != null) {
            throw new IllegalArgumentException(
                    "Meal slot ID already exists.");
        }

        slots.add(newSlot);
    }

    /**
     * Removes a meal slot identified by its ID.
     *
     * @param id ID of the slot to remove
     * @return true if removed, false if no matching slot exists
     */
    public boolean removeSlot(String id) {
        return slots.removeIf(slot -> slot.getId().equals(id));
    }

    /**
     * Renames a meal slot while preserving its original ID.
     *
     * Since MealSlot is immutable, it is replaced with a new
     * instance at the same position.
     *
     * @param id ID of the slot to rename
     * @param newName new display name
     * @return true if renamed, false if the slot does not exist
     * @throws IllegalArgumentException if the new name is invalid
     */
    public boolean renameSlot(String id, String newName) {
        for (int i = 0; i < slots.size(); i++) {
            MealSlot slot = slots.get(i);

            if (slot.getId().equals(id)) {
                MealSlot renamed = new MealSlot(id, newName);
                slots.set(i, renamed);
                return true;
            }
        }
        return false;
    }

    /**
     * Moves a meal slot to a specified position.
     *
     * Positions are zero-based: 0 represents the first slot.
     *
     * @param id ID of the slot to move
     * @param newIndex desired position
     * @return true if moved, false if the slot does not exist
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public boolean moveSlot(String id, int newIndex) {
        if (newIndex < 0 || newIndex >= slots.size()) {
            throw new IndexOutOfBoundsException(
                    "Invalid meal slot position.");
        }

        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i).getId().equals(id)) {

                // Remove the slot from its old position.
                MealSlot slot = slots.remove(i);

                // Insert it at the requested position.
                slots.add(newIndex, slot);
                return true;
            }
        }

        return false;
    }
}
