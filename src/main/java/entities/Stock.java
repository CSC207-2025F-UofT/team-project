package entities;

/**
 * An entity representing a stock. This entity is immutable once created, so it has no mutator
 * methods.
 */
public class Stock {
  private final String ticker;

  /**
   * Constructs a new Stock object.
   *
   * @param ticker The ticker of the stock
   */
  public Stock(String ticker) {
    if ("".equals(ticker)) {
      throw new IllegalArgumentException("Ticker cannot be empty");
    }

    this.ticker = ticker;
  }

  public String getTicker() {
    return ticker;
  }
}
