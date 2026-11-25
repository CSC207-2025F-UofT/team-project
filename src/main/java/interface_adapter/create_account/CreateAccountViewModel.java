package interface_adapter.create_account;

/** Display state for the Create Account View. */
public final class CreateAccountViewModel {
    private String accountName = "";
    private String message = "";
    private boolean successful;

    public String getAccountName() { return accountName; }
    public String getMessage() { return message; }
    public boolean isSuccessful() { return successful; }

    public void setAccountName(String accountName) { this.accountName = accountName; }
    public void setMessage(String message) { this.message = message; }
    public void setSuccessful(boolean successful) { this.successful = successful; }
}
