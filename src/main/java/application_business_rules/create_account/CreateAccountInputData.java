package application_business_rules.create_account;

import entity.AccountType;
import java.math.BigDecimal;

/** Input prepared by the Create Account controller. */
public final class CreateAccountInputData {
    private final int userProfileId;
    private final String accountName;
    private final AccountType accountType;
    private final BigDecimal initialBalance;
    private final String currencyCode;

    public CreateAccountInputData(int userProfileId, String accountName,
                                  AccountType accountType, BigDecimal initialBalance,
                                  String currencyCode) {
        this.userProfileId = userProfileId;
        this.accountName = accountName;
        this.accountType = accountType;
        this.initialBalance = initialBalance;
        this.currencyCode = currencyCode;
    }

    public int getUserProfileId() { return userProfileId; }
    public String getAccountName() { return accountName; }
    public AccountType getAccountType() { return accountType; }
    public BigDecimal getInitialBalance() { return initialBalance; }
    public String getCurrencyCode() { return currencyCode; }
}
