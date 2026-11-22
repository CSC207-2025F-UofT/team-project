package interface_adapter.change_currency;

import java.math.BigDecimal;

public final class ChangeCurrencyViewModel {
    private int accountId;
    private String currencyCode = "";
    private BigDecimal convertedBalance = BigDecimal.ZERO;
    private String message = "";
    private boolean error;

    public int getAccountId() { return accountId; }
    public String getCurrencyCode() { return currencyCode; }
    public BigDecimal getConvertedBalance() { return convertedBalance; }
    public String getMessage() { return message; }
    public boolean isError() { return error; }

    void setSuccess(int id, String currency, BigDecimal balance) {
        accountId = id; currencyCode = currency; convertedBalance = balance;
        message = "Currency changed successfully."; error = false;
    }
    void setError(String value) { message = value; error = true; }
}
