package interface_adapter.logout;

import use_case.logout.LogOutInputBoundary;

public final class LogOutController {
    private final LogOutInputBoundary input;

    public LogOutController(LogOutInputBoundary input) {
        this.input = input;
    }

    public void logOut() {
        input.execute();
    }
}
