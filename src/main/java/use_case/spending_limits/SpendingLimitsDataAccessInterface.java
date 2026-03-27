package use_case.spending_limits;

import java.util.Map;

public interface SpendingLimitsDataAccessInterface {
    void saveUserSpendingLimits(String username, Map<String,Double> spendingLimits);
    Map<String,Double> getSpendingLimitsByUsername(String username);
}
