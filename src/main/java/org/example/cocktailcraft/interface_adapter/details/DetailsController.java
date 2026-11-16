package org.example.cocktailcraft.interface_adapter.details;

import org.example.cocktailcraft.use_case.details.DetailsInputBoundary;
import org.example.cocktailcraft.use_case.details.DetailsInputData;

public class DetailsController {
    private final DetailsInputBoundary interactor;

    public DetailsController(DetailsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String cocktailId) {
        interactor.execute(new DetailsInputData(cocktailId));
    }
}
