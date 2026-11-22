package view.dashboard;

import interface_adapter.dashboard.DashboardController;
import interface_adapter.dashboard.DashboardItemState;
import interface_adapter.dashboard.DashboardStateCatalog;

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

    public void registerAll(DashboardController dashboardController) {
        register(DashboardStateCatalog.TableState.class, new TableRenderer());
        register(DashboardStateCatalog.NestedState.class, new NestedRenderer(this));
        register(DashboardStateCatalog.DropdownState.class, new DropdownRenderer(dashboardController));
    }
    private <T extends DashboardItemState> void register(Class<T> type, DashboardRenderer dashboardRenderer) {
        dashboardRenderers.put(type, dashboardRenderer);
    }
}
