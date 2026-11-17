package app;

import data_access.FrankfurterExchangeRateGateway;
import use_case.exchange_rate.ExchangeRateDataAccessInterface;
import use_case.exchange_rate.ExchangeRateUnavailableException;

/**
 * Quick check that our Java code can call the exchange rate API. Run this main method in IntelliJ;
 * it needs an internet connection. (Milestone 2: "runnable code that can make calls to any external
 * APIs used by the project.")
 */
public final class ExchangeRateApiDemo {

  private ExchangeRateApiDemo() {}

  /**
   * Prints a few live exchange rates.
   *
   * @param args unused
   */
  public static void main(String[] args) {
    final ExchangeRateDataAccessInterface rates = new FrankfurterExchangeRateGateway();
    try {
      System.out.println("Supported currencies: " + rates.getSupportedCurrencies());
      System.out.println("1 CAD = " + rates.getRate("CAD", "JPY") + " JPY");
      System.out.println("1 CAD = " + rates.getRate("CAD", "EUR") + " EUR");
      System.out.println("1 USD = " + rates.getRate("USD", "CAD") + " CAD");
    } catch (ExchangeRateUnavailableException ex) {
      System.out.println("API call failed: " + ex.getMessage());
    }
  }
}
