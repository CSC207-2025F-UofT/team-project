package view.dashboard;

import interface_adapter.dashboard.DashboardStateCatalog;

import javax.swing.*;

/**
 * Render a nested state recursively.
 */
public class NestedRenderer implements DashboardRenderer<DashboardStateCatalog.NestedState> {
    private final DashboardRendererRegistry dashboardRendererRegistry;

    NestedRenderer(DashboardRendererRegistry dashboardRendererRegistry) {
        super();
        this.dashboardRendererRegistry = dashboardRendererRegistry;
    }

    @Override
    public JComponent render(DashboardStateCatalog.NestedState state) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        state.dashboardStates().forEach(child -> {
            panel.add(dashboardRendererRegistry.resolve(child.getClass()).render(child));
        });
        return panel;
    }
}
