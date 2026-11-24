package use_case.change_currency;

import entity.Account;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Changes one account's currency and converts its balance using the requested date's rate. */
public final class ChangeCurrencyInteractor implements ChangeCurrencyInputBoundary {
    private final CurrencyDataAccessInterface dataAccess;
    private final ChangeCurrencyOutputBoundary presenter;

    public ChangeCurrencyInteractor(CurrencyDataAccessInterface dataAccess,
                                    ChangeCurrencyOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(ChangeCurrencyInputData inputData) {
        try {
            if (inputData.getTargetCurrency() == null
                    || !inputData.getTargetCurrency().matches("[A-Z]{3}")) {
                presenter.prepareFailView("Enter a valid three-letter currency code.");
                return;
            }
            Account account = dataAccess.getAccount(inputData.getAccountId());
            if (account == null) {
                presenter.prepareFailView("Account could not be found.");
                return;
            }
            LocalDate date = inputData.getEffectiveDate() == null
                ? LocalDate.now() : inputData.getEffectiveDate();
            BigDecimal rate = dataAccess.getExchangeRate(
                account.getCurrency(), inputData.getTargetCurrency(), date);
            account.changeCurrency(inputData.getTargetCurrency(), rate);
            dataAccess.saveAccount(account);
            presenter.prepareSuccessView(new ChangeCurrencyOutputData(
                account.getAccountID(), account.getCurrency(), account.getBalance()));
        } catch (IOException exception) {
            presenter.prepareFailView("Could not retrieve or save the exchange rate: "
                + exception.getMessage());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            presenter.prepareFailView("Currency conversion was interrupted.");
        } catch (IllegalArgumentException exception) {
            presenter.prepareFailView(exception.getMessage());
        }
    }
}
