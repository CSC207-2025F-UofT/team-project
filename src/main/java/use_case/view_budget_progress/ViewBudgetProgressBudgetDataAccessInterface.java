package use_case.view_budget_progress;
import java.math.BigDecimal;
public interface ViewBudgetProgressBudgetDataAccessInterface {
    /** Returns this user's recurring daily limit, or null if unset. */
    BigDecimal getDailyLimit(String username);
}
