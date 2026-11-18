package view.dashboard;

import entity.dashboard.DashboardItem;
import interface_adapter.dashboard.DashboardItemState;

import javax.swing.*;

/**
 * Randerer for a component on the dashboard.
 */
public interface DashboardRenderer {

    /**
     * Render a component on the dashboard
     *
     * @param itemState dashboard item with data
     * @return component on the dashboard
     */
    JComponent render(DashboardItemState itemState);
}
