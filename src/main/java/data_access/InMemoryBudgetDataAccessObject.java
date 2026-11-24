package data_access;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import use_case.view_budget_progress.ViewBudgetProgressBudgetDataAccessInterface;

/** Per-user budgets for this application run. No default budget is assumed. */
public class InMemoryBudgetDataAccessObject implements ViewBudgetProgressBudgetDataAccessInterface {
    private final Map<String, BigDecimal> limits = new HashMap<>();
    public void saveDailyLimit(String username, BigDecimal limit) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("A username is required.");
        }
        if (limit == null || limit.signum() <= 0) {
            throw new IllegalArgumentException("The daily budget must be positive.");
        }
        try {
            limits.put(username, limit.setScale(2, RoundingMode.UNNECESSARY));
        }
        catch (ArithmeticException exception) {
            throw new IllegalArgumentException("The budget must be representable in cents.", exception);
        }
    }
    @Override
    public BigDecimal getDailyLimit(String username) { return limits.get(username); }
}
