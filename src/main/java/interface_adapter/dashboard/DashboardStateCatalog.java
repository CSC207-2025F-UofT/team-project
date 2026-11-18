package interface_adapter.dashboard;

import java.util.List;

/**
 * A catalog for dashboard component states.
 */
public class DashboardStateCatalog {
    public record TableState(
            List<String> title,
            List<String> columns,
            List<List<String>> rows
    ) implements DashboardItemState {}
}
