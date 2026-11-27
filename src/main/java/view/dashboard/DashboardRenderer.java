package view.dashboard;

import interface_adapter.dashboard.DashboardItemState;

import javax.swing.*;

/**
 * Renderer for a component on the dashboard.
 */
public interface DashboardRenderer<T extends DashboardItemState> {

    /**
     * Render a component on the dashboard
     *
     * @param itemState dashboard item with data
     * @return component on the dashboard
     */
    JComponent render(T itemState);
}
