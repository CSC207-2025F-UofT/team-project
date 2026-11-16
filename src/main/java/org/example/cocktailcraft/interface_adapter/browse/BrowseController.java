package org.example.cocktailcraft.interface_adapter.browse;

import org.example.cocktailcraft.use_case.browse.BrowseInputBoundary;

public class BrowseController {
    private final BrowseInputBoundary interactor;

    public BrowseController(BrowseInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        interactor.execute();
    }
}
