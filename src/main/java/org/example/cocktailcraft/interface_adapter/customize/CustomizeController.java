package org.example.cocktailcraft.interface_adapter.customize;

import org.example.cocktailcraft.use_case.customize.CustomizeInputBoundary;
import org.example.cocktailcraft.use_case.customize.CustomizeInputData;

public class CustomizeController {
    private final CustomizeInputBoundary interactor;

    public CustomizeController(CustomizeInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String originalCocktailId) {
        interactor.execute(new CustomizeInputData(originalCocktailId));
    }
}
