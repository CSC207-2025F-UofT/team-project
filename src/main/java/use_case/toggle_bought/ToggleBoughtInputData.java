package use_case.toggle_bought;

import java.time.LocalDate;

/**
 * Input data for toggling the bought flag on one shopping item.
 */
public class ToggleBoughtInputData {

    private final LocalDate weekStart;
    private final String ingredientName;

    /**
     * Creates input data.
     *
     * @param weekStart the Monday that starts the week
     * @param ingredientName the ingredient display name to toggle
     */
    public ToggleBoughtInputData(LocalDate weekStart, String ingredientName) {
        this.weekStart = weekStart;
        this.ingredientName = ingredientName;
    }

    /**
     * Returns the week start.
     *
     * @return the week start
     */
    public LocalDate getWeekStart() {
        return weekStart;
    }

    /**
     * Returns the ingredient name text.
     *
     * @return the ingredient name
     */
    public String getIngredientName() {
        return ingredientName;
    }
}
