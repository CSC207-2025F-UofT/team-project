package interface_adapter.dashboard;

import interface_adapter.ViewModel;

/**
 * View model for analytics dashboard.
 */
public class DashboardViewModel extends ViewModel<DashboardState> {
    public DashboardViewModel() {
        super("Dashboard");
        setState(new DashboardState());
    }
}
