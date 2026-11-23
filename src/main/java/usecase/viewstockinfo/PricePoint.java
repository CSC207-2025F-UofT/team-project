package usecase.viewstockinfo;

import java.time.LocalDate;

/**
 * A closing-price point for one trading day on the historical price chart.
 */
public class PricePoint {
  private final LocalDate date;
  private final double close;

  /**
   * Creates a new PricePoint object.
   *
   * @param date The trading date in the market's time zone
   * @param close The closing price on that date
   */
  public PricePoint(LocalDate date, double close) {
    this.date = date;
    this.close = close;
  }

  public LocalDate getDate() {
    return date;
  }

  public double getClose() {
    return close;
  }
}
