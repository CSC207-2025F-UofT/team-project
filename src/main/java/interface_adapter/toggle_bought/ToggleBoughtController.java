package interface_adapter.toggle_bought;

import use_case.toggle_bought.ToggleBoughtInputBoundary;
import use_case.toggle_bought.ToggleBoughtInputData;

import java.time.LocalDate;

/**
 * Controller for toggling bought on a shopping item.
 */
public class ToggleBoughtController {

    private final ToggleBoughtInputBoundary interactor;

    /**
     * Creates the controller.
     *
     * @param interactor the toggle interactor
     */
    public ToggleBoughtController(ToggleBoughtInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Toggles the bought flag for one ingredient.
     *
     * @param weekStart the Monday that starts the week
     * @param ingredientName the ingredient display name
     */
    public void execute(LocalDate weekStart, String ingredientName) {
        interactor.execute(new ToggleBoughtInputData(weekStart, ingredientName));
    }
}
