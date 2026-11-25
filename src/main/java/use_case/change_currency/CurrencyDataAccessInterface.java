package use_case.change_currency;

import entity.Account;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Implemented outside the use-case layer by the chosen currency/data gateway. */
public interface CurrencyDataAccessInterface {
    Account getAccount(int accountId);
    BigDecimal getExchangeRate(String sourceCurrency, String targetCurrency, LocalDate date) throws java.io.IOException, InterruptedException;
    void saveAccount(Account account);
}
