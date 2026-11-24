package use_case.change_currency;

import java.time.LocalDate;

public final class ChangeCurrencyInputData {
    private final int accountId;
    private final String targetCurrency;
    private final LocalDate effectiveDate;

    public ChangeCurrencyInputData(int accountId, String targetCurrency, LocalDate effectiveDate) {
        this.accountId = accountId;
        this.targetCurrency = targetCurrency;
        this.effectiveDate = effectiveDate;
    }
    public int getAccountId() { return accountId; }
    public String getTargetCurrency() { return targetCurrency; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
}
