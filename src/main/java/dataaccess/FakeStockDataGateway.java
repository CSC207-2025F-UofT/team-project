package dataaccess;

import entity.CompanyProfile;
import entity.Money;
import entity.PricePoint;
import entity.Quote;
import entity.Stock;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import usecase.StockDataGateway;
import usecase.StockDataUnavailableException;

/**
 * A stock data gateway that serves real responses recorded from Finnhub and Twelve Data, so
 * tests and the app can run without calling the real services. The recorded files are in
 * {@code src/main/resources/fake-stock-data/}; {@code spike/MarketDataSpike.java} re-records
 * them.
 */
public class FakeStockDataGateway implements StockDataGateway {

  /** The tickers that have recorded data. */
  public static final List<String> TICKERS = List.of("AAPL", "MSFT", "GOOGL", "AMZN", "NVDA");

  private static final String DATA_DIR = "/fake-stock-data/";

  private final Map<String, Quote> quotes = new HashMap<>();
  private final Map<String, CompanyProfile> profiles = new HashMap<>();

  /**
   * Creates the gateway and loads the recorded data for every ticker in {@link #TICKERS}.
   *
   * @throws IllegalStateException if a recorded file is missing or can't be read
   */
  public FakeStockDataGateway() {
    for (String ticker : TICKERS) {
      quotes.put(ticker, parseQuote(ticker, readJson(ticker + "-quote.json")));
      profiles.put(ticker, parseProfile(readJson(ticker + "-profile.json")));
    }
  }

  @Override
  public List<Stock> search(String query) throws StockDataUnavailableException {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public Quote getQuote(String ticker) throws StockDataUnavailableException {
    Quote quote = quotes.get(ticker);
    if (quote == null) {
      throw new StockDataUnavailableException("No quote for ticker: " + ticker);
    }
    return quote;
  }

  /**
   * Moves a stock's price, for tests. The day's high and low are both set to the new price, so
   * a limit order's fill depends only on the price the test chose and not on the recorded
   * range. The open, previous close and time are unchanged.
   *
   * @param ticker one of {@link #TICKERS}
   * @param price the new price; greater than zero
   * @throws IllegalArgumentException if {@code ticker} has no recorded data or {@code price} is
   *     not greater than zero
   */
  public void setPrice(String ticker, Money price) {
    Quote old = quotes.get(ticker);
    if (old == null) {
      throw new IllegalArgumentException("No recorded data for ticker: " + ticker);
    }
    quotes.put(
        ticker,
        new Quote(ticker, price, old.open(), price, price, old.previousClose(), old.time()));
  }

  @Override
  public CompanyProfile getProfile(String ticker) throws StockDataUnavailableException {
    CompanyProfile profile = profiles.get(ticker);
    if (profile == null) {
      throw new StockDataUnavailableException("No profile for ticker: " + ticker);
    }
    return profile;
  }

  @Override
  public List<PricePoint> getDailyHistory(String ticker, LocalDate from, LocalDate to)
      throws StockDataUnavailableException {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /** Converts a Finnhub {@code /quote} response. */
  private static Quote parseQuote(String ticker, JSONObject json) {
    return new Quote(
        ticker,
        money(json, "c"),
        money(json, "o"),
        money(json, "h"),
        money(json, "l"),
        money(json, "pc"),
        Instant.ofEpochSecond(json.getLong("t")));
  }

  /** Converts a Finnhub {@code /stock/profile2} response. */
  private static CompanyProfile parseProfile(JSONObject json) {
    Stock stock =
        new Stock(
            json.getString("ticker"),
            json.getString("name"),
            exchangeName(json.getString("exchange")));
    // Finnhub gives the market cap in millions of dollars
    BigDecimal marketCap =
        new BigDecimal(json.get("marketCapitalization").toString()).movePointRight(6);
    return new CompanyProfile(
        stock,
        json.optString("finnhubIndustry", ""),
        Money.of(marketCap.setScale(2, RoundingMode.HALF_EVEN).toPlainString()));
  }

  /**
   * Shortens Finnhub's exchange name to the one users know, for example {@code "NASDAQ NMS -
   * GLOBAL MARKET"} to {@code "NASDAQ"}. Names it doesn't recognize are kept as they are.
   */
  static String exchangeName(String finnhubExchange) {
    if (finnhubExchange.startsWith("NASDAQ")) {
      return "NASDAQ";
    }
    if (finnhubExchange.startsWith("NEW YORK STOCK EXCHANGE")) {
      return "NYSE";
    }
    return finnhubExchange;
  }

  /**
   * Reads a price, rounding it to whole cents. Goes through the number's text so that, for
   * example, 346.715 rounds to 346.72 rather than being misread as 346.71499999.
   */
  private static Money money(JSONObject json, String key) {
    BigDecimal value = new BigDecimal(json.get(key).toString());
    return Money.of(value.setScale(2, RoundingMode.HALF_EVEN).toPlainString());
  }

  private static JSONObject readJson(String fileName) {
    try (InputStream in = FakeStockDataGateway.class.getResourceAsStream(DATA_DIR + fileName)) {
      if (in == null) {
        throw new IllegalStateException("Missing recorded file: " + DATA_DIR + fileName);
      }
      return new JSONObject(new String(in.readAllBytes(), StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new IllegalStateException("Can't read recorded file: " + DATA_DIR + fileName, e);
    }
  }
}
