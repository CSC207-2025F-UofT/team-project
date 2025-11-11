package auth.interface_adapters.controllers;

import auth.use_case.DashboardInteractor;

public class DashboardController {

    private final DashboardInteractor dashboardInteractor;

    public DashboardController(DashboardInteractor dashboardInteractor) {
        this.dashboardInteractor = dashboardInteractor;
    };
}
