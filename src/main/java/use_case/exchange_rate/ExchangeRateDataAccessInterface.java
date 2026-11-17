package use_case.exchange_rate;

import java.util.List;

/**
 * Gives use cases access to current exchange rates without knowing where they come from. Shared by
 * any use case that needs to convert money (for example, logging an expense paid in a foreign
 * currency). The real implementation calls the Frankfurter API; tests can use a fake that returns
 * fixed rates.
 */
public interface ExchangeRateDataAccessInterface {

  /**
   * Returns how many units of {@code toCurrency} one unit of {@code fromCurrency} buys today. For
   * example, getRate("CAD", "JPY") might return 111.7.
   *
   * @param fromCurrency ISO 4217 code to convert from
   * @param toCurrency ISO 4217 code to convert to
   * @return the exchange rate
   * @throws ExchangeRateUnavailableException if the rate cannot be retrieved
   */
  double getRate(String fromCurrency, String toCurrency) throws ExchangeRateUnavailableException;

  /**
   * Returns the currency codes that rates are available for, sorted alphabetically.
   *
   * @return supported ISO 4217 codes
   * @throws ExchangeRateUnavailableException if the list cannot be retrieved
   */
  List<String> getSupportedCurrencies() throws ExchangeRateUnavailableException;
}
