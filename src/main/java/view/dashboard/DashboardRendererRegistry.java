package view.dashboard;

import entity.dashboard.DashboardItem;
import interface_adapter.dashboard.DashboardItemState;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for the renderers for each dashboard item
 */
public class DashboardRendererRegistry {
    private final Map<Class<?>, DashboardRenderer> dashboardRenderers = new HashMap<>();

    public DashboardRenderer resolve(Class<?> dashboardItemType) {
        return dashboardRenderers.get(dashboardItemType);
    }

    public <T extends DashboardItemState> void register(Class<T> type, DashboardRenderer dashboardRenderer) {
        dashboardRenderers.put(type, dashboardRenderer);
    }
}
