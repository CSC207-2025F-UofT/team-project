package interface_adapter.meal_plan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * State of the meal plan page.
 */
public class MealPlanState {

    private LocalDate weekStart;
    private List<MealPlanSlotViewData> slots = new ArrayList<>();
    private String message = "";
    private String error = "";

    /**
     * Returns the Monday of the week shown.
     *
     * @return the week start, or null before the first load
     */
    public LocalDate getWeekStart() {
        return weekStart;
    }

    /**
     * Sets the Monday of the week shown.
     *
     * @param weekStart the week start
     */
    public void setWeekStart(LocalDate weekStart) {
        this.weekStart = weekStart;
    }

    /**
     * Returns all 21 cells, Monday breakfast first.
     *
     * @return the cells
     */
    public List<MealPlanSlotViewData> getSlots() {
        return slots;
    }

    /**
     * Replaces the cells.
     *
     * @param slots the cells, Monday breakfast first
     */
    public void setSlots(List<MealPlanSlotViewData> slots) {
        this.slots = new ArrayList<>(slots);
    }

    /**
     * Returns the info message to show, or an empty string.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the info message.
     *
     * @param message the message, or null for none
     */
    public void setMessage(String message) {
        this.message = message == null ? "" : message;
    }

    /**
     * Returns the error to show, or an empty string.
     *
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error.
     *
     * @param error the error, or null for none
     */
    public void setError(String error) {
        this.error = error == null ? "" : error;
    }
}
