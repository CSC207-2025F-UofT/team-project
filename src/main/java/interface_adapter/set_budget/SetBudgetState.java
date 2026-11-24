package interface_adapter.set_budget;

/**
 * The state for the Set Budget View.
 */
public class SetBudgetState {
    private String username = "";
    private String dailyLimit = "";
    private String balance = "";
    private String errorMessage = "";
    private String successMessage = "";

    public SetBudgetState(SetBudgetState copy) {
        username = copy.username;
        dailyLimit = copy.dailyLimit;
        balance = copy.balance;
        errorMessage = copy.errorMessage;
        successMessage = copy.successMessage;
    }

    public SetBudgetState() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(String dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}