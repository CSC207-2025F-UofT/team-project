package use_case.load_shopping_list;

import java.time.LocalDate;

/**
 * Input data for loading a shopping list.
 */
public class LoadShoppingListInputData {

    private final LocalDate weekStart;

    /**
     * Creates input data.
     *
     * @param weekStart the Monday that starts the week
     */
    public LoadShoppingListInputData(LocalDate weekStart) {
        this.weekStart = weekStart;
    }

    /**
     * Returns the week start.
     *
     * @return the week start
     */
    public LocalDate getWeekStart() {
        return weekStart;
    }
}
