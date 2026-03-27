package use_case.spending_limits;

import java.util.Map;

public interface SpendingLimitsInputBoundary {
    void execute();
    void loadLimits(String username);
    void saveLimits(String username, Map<String, Double> limits);
}
