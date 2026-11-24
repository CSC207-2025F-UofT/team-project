package interface_adapter.change_currency;

import use_case.change_currency.ChangeCurrencyInputBoundary;
import use_case.change_currency.ChangeCurrencyInputData;
import java.time.LocalDate;

public final class ChangeCurrencyController {
    private final ChangeCurrencyInputBoundary inputBoundary;
    public ChangeCurrencyController(ChangeCurrencyInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }
    public void execute(int accountId, String targetCurrency, LocalDate effectiveDate) {
        inputBoundary.execute(new ChangeCurrencyInputData(accountId,
            targetCurrency == null ? null : targetCurrency.trim().toUpperCase(), effectiveDate));
    }
}
