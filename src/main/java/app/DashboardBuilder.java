package app;

import javax.swing.JPanel;

import service.Backend;
import service.Frontend;
import view.MainDashboardView;

/**
 * Builds admin, staff and user dashboard views.
 */
public class DashboardBuilder {

    private final JPanel cardPanel;
    private final Frontend frontend;
    private final Backend backend;

    public DashboardBuilder(JPanel cardPanel, Frontend frontend, Backend backend) {
        this.cardPanel = cardPanel;
        this.frontend = frontend;
        this.backend = backend;
    }

    /**
     * Creates and registers all dashboard views.
     */
    public void build() {

        JPanel admin = new MainDashboardView(frontend, backend, "admin");
        JPanel staff = new MainDashboardView(frontend, backend, "staff");
        JPanel user = new MainDashboardView(frontend, backend, "user");

        cardPanel.add(admin, "adminDashboard");
        cardPanel.add(staff, "staffDashboard");
        cardPanel.add(user, "userDashboard");
    }
}
