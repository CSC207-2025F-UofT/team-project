package use_case.set_budget;

/**
 * The output data for the Set Budget Use Case.
 */
public class SetBudgetOutputData {

    private final String username;
    private final String dailyLimitText;
    private final String balanceText;
    
    public SetBudgetOutputData(String username, String dailyLimitText, String balanceText) {
        this.username = username;
        this.dailyLimitText = dailyLimitText;
        this.balanceText = balanceText;
    }

    public String getUsername() {
        return username;
    }

    public String getDailyLimitText() {
        return dailyLimitText;
    }

    public String getBalanceText() {
        return balanceText;
    }
}
