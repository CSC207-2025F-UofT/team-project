package application_business_rules.change_currency;

import java.math.BigDecimal;

public final class ChangeCurrencyOutputData {
    private final int accountId;
    private final String currencyCode;
    private final BigDecimal convertedBalance;

    public ChangeCurrencyOutputData(int accountId, String currencyCode, BigDecimal convertedBalance) {
        this.accountId = accountId;
        this.currencyCode = currencyCode;
        this.convertedBalance = convertedBalance;
    }
    public int getAccountId() { return accountId; }
    public String getCurrencyCode() { return currencyCode; }
    public BigDecimal getConvertedBalance() { return convertedBalance; }
}
