package application_business_rules.create_account;

/** Result passed from the Create Account Interactor to its Output Boundary. */
public final class CreateAccountOutputData {
    private final int accountId;
    private final String accountName;
    private final boolean useCaseFailed;

    public CreateAccountOutputData(int accountId, String accountName, boolean useCaseFailed) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.useCaseFailed = useCaseFailed;
    }

    public int getAccountId() { return accountId; }
    public String getAccountName() { return accountName; }
    public boolean isUseCaseFailed() { return useCaseFailed; }
}
