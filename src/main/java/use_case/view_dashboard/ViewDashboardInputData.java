package use_case.view_dashboard;

/** Whose dashboard to show. */
public class ViewDashboardInputData {

    private final String username;

    public ViewDashboardInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
