package use_case.exchange_rate;

/**
 * Thrown when an exchange rate cannot be retrieved, for example because there is no internet
 * connection or the currency is not supported.
 */
public class ExchangeRateUnavailableException extends Exception {

  /**
   * Creates the exception.
   *
   * @param message what went wrong
   */
  public ExchangeRateUnavailableException(String message) {
    super(message);
  }

  /**
   * Creates the exception.
   *
   * @param message what went wrong
   * @param cause the underlying error
   */
  public ExchangeRateUnavailableException(String message, Throwable cause) {
    super(message, cause);
  }
}
