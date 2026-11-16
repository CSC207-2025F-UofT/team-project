package org.example.cocktailcraft.interface_adapter.logout;

import org.example.cocktailcraft.use_case.logout.LogoutInputBoundary;

public class LogoutController {
    private final LogoutInputBoundary interactor;

    public LogoutController(LogoutInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        interactor.execute();
    }
}
