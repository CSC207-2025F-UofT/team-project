package entity.dashboard;

/**
 * general dashboard item entity.
 */
public abstract class DashboardItem {
    public String title;
    public enum PresentationType {
        RANKING_TABLE, LINE_GRAPH, HIGHLIGHT
    }
    public PresentationType presentationType;
    protected DashboardItem(String title) {
        this.title = title;
    }

}
