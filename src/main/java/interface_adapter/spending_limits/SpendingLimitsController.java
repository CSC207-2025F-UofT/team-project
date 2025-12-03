package interface_adapter.spending_limits;

import use_case.spending_limits.SpendingLimitsInputBoundary;

import java.util.Map;

public class SpendingLimitsController {
    private final SpendingLimitsInputBoundary interactor;

    public SpendingLimitsController(SpendingLimitsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void loadLimits(String username) {
        interactor.loadLimits(username);
    }

    public void saveLimits(String username, Map<String, Double> limits) {
        interactor.saveLimits(username, limits);
    }

}
