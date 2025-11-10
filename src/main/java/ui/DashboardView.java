package ui;

import auth.interface_adapters.controllers.DashboardController;

import javax.swing.*;

public class DashboardView extends JFrame {

    private final DashboardController dashboardController;
    private final Runnable onLogout;
    private final String username;

    private final int HOME_TAB = 0;
    private final int NEWS_TAB = 1;
    private final int Track_TAB = 2;
    private final int

    public DashboardView(DashboardController dashboardController, Runnable onLogout, String username){
        this.dashboardController = dashboardController;
        this.onLogout = onLogout;
        this.username = username;
    }
}
