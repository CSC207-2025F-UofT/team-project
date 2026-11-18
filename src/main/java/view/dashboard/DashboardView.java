package view.dashboard;

import interface_adapter.dashboard.DashboardController;
import interface_adapter.dashboard.DashboardItemState;
import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import use_case.dashboard.AnalyticsType;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

public class DashboardView extends JPanel implements PropertyChangeListener {

    private final DashboardViewModel dashboardViewModel;
    private final DashboardController controller;
    private static final Map<String, AnalyticsType> TYPES = Map.of(
            "Most Visited Cities", AnalyticsType.MOST_VISITED_CITIES
    );
    private final JPanel optionPanel = new JPanel();
    private final JPanel analyticsPanel = new JPanel();
    private final List<JComponent> analytics = List.of();

    public DashboardView(DashboardViewModel dashboardViewModel, DashboardController dashboardController) {
        this.dashboardViewModel = dashboardViewModel;
        this.controller = dashboardController;
        dashboardViewModel.addPropertyChangeListener(this);

        initOptionPanel();
        this.add(analyticsPanel);
        refreshAnalyticsPanel();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        DashboardState dashboardState = dashboardViewModel.getState();
        addAnalytics(dashboardState);
        refreshAnalyticsPanel();
    }

    private void addAnalytics(DashboardState dashboardState) {
        for (DashboardItemState dashboardItemState : dashboardState.getItemStates()) {
            DashboardRenderer renderer = new DashboardRendererRegistry().resolve(dashboardItemState.getClass());
            JComponent component = renderer.render(dashboardItemState);
            this.analytics.add(component);
        }
    }

    private void initOptionPanel() {
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
        optionPanel.add(new JLabel("What kind of analytics do you want to view?"));
        JComboBox<String> dropdown = new JComboBox<>(TYPES.keySet().toArray(new String[0]));
        dropdown.addActionListener(e -> {
            String selected = (String)dropdown.getSelectedItem();
            controller.requestAnalytics(TYPES.get(selected));
        });
        optionPanel.add(dropdown);
        this.add(optionPanel);
    }

    private void refreshAnalyticsPanel() {
        analyticsPanel.setLayout(new BoxLayout(analyticsPanel, BoxLayout.Y_AXIS));
        analyticsPanel.removeAll();
        for (JComponent component : analytics) {
            analyticsPanel.add(component);
        }
    }


}
