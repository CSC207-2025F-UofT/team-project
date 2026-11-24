package use_case.set_budget;

/**
 * The input data for the Set Budget Use Case.
 */
public class SetBudgetInputData {

    private final String username;
    private final String dailyLimitText;
    private final String balanceText;

    public SetBudgetInputData(String username, String dailyLimitText, String balanceText) {
        this.username = username;
        this.dailyLimitText = dailyLimitText;
        this.balanceText = balanceText;
    }

    String getUsername() {
        return username;
    }

    String getDailyLimitText() {
        return dailyLimitText;
    }

    String getBalanceText() {
        return balanceText;
    }
}