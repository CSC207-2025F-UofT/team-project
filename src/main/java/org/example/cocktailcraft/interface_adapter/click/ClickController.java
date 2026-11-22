package org.example.cocktailcraft.interface_adapter.click;

import org.example.cocktailcraft.use_case.click.ClickInputData;
import org.example.cocktailcraft.use_case.click.ClickInputBoundary;

public class ClickController {
    private final ClickInputBoundary clickUseCaseInteractor;

    public ClickController(ClickInputBoundary clickUseCaseInteractor) {
        this.clickUseCaseInteractor = clickUseCaseInteractor;
    }

    /**
     * Executes the Click Use Case.
     * @param cocktailID the cocktail of the user clicking
     */
    public void execute(String cocktailID) {
        final ClickInputData clickInputData = new ClickInputData(cocktailID);

        clickUseCaseInteractor.execute(clickInputData);
    }
}
