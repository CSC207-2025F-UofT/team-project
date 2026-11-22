package application_business_rules.change_currency;

import entity.Account;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Implemented outside the use-case layer by the chosen currency/data gateway. */
public interface CurrencyDataAccessInterface {
    Account getAccount(int accountId);
    BigDecimal getExchangeRate(String sourceCurrency, String targetCurrency, LocalDate date);
    void saveAccount(Account account);
}
