package data_access;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.exchange_rate.ExchangeRateDataAccessInterface;
import use_case.exchange_rate.ExchangeRateUnavailableException;

/**
 * Gets exchange rates from the free Frankfurter API (https://frankfurter.dev). No API key is
 * needed. Rates come from the European Central Bank and are updated once per working day. Only
 * about 30 major currencies are supported; call {@link #getSupportedCurrencies()} to get the list
 * for a dropdown.
 */
public class FrankfurterExchangeRateGateway implements ExchangeRateDataAccessInterface {

  /** The public Frankfurter server. */
  public static final String DEFAULT_BASE_URL = "https://api.frankfurter.dev/v1";

  private static final int HTTP_OK = 200;
  private static final int HTTP_NOT_FOUND = 404;
  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private final String baseUrl;
  private final HttpClient client;

  /** Creates a gateway that talks to the public Frankfurter server. */
  public FrankfurterExchangeRateGateway() {
    this(DEFAULT_BASE_URL);
  }

  /**
   * Creates a gateway that talks to the given server (useful if you self-host Frankfurter).
   *
   * @param baseUrl the API base URL, without a trailing slash
   */
  public FrankfurterExchangeRateGateway(String baseUrl) {
    this.baseUrl = baseUrl;
    this.client = HttpClient.newBuilder().connectTimeout(TIMEOUT).build();
  }

  @Override
  public double getRate(String fromCurrency, String toCurrency)
      throws ExchangeRateUnavailableException {
    final String from = fromCurrency.toUpperCase();
    final String to = toCurrency.toUpperCase();
    if (from.equals(to)) {
      return 1.0;
    }
    final String body = get("/latest?base=" + from + "&symbols=" + to);
    return parseRate(body, to);
  }

  @Override
  public List<String> getSupportedCurrencies() throws ExchangeRateUnavailableException {
    return parseCurrencies(get("/currencies"));
  }

  private String get(String path) throws ExchangeRateUnavailableException {
    final HttpRequest request =
        HttpRequest.newBuilder(URI.create(baseUrl + path))
            .timeout(TIMEOUT)
            .header("Accept", "application/json")
            .GET()
            .build();
    final HttpResponse<String> response;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException ex) {
      throw new ExchangeRateUnavailableException("Could not reach the exchange rate service", ex);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      throw new ExchangeRateUnavailableException("Exchange rate request was interrupted", ex);
    }
    if (response.statusCode() == HTTP_NOT_FOUND) {
      throw new ExchangeRateUnavailableException("Currency not supported");
    }
    if (response.statusCode() != HTTP_OK) {
      throw new ExchangeRateUnavailableException(
          "Exchange rate service returned status " + response.statusCode());
    }
    return response.body();
  }

  /**
   * Reads the rate out of a response such as {@code
   * {"amount":1.0,"base":"CAD","date":"2026-11-16","rates":{"JPY":111.72}}}.
   *
   * @param body the response body
   * @param toCurrency the currency whose rate to read
   * @return the rate
   * @throws ExchangeRateUnavailableException if the body is not in the expected format
   */
  static double parseRate(String body, String toCurrency) throws ExchangeRateUnavailableException {
    try {
      return new JSONObject(body).getJSONObject("rates").getDouble(toCurrency);
    } catch (JSONException ex) {
      throw new ExchangeRateUnavailableException(
          "Unexpected response from exchange rate service", ex);
    }
  }

  /**
   * Reads the currency codes out of a response such as {@code {"AUD":"Australian
   * Dollar","CAD":"Canadian Dollar"}}.
   *
   * @param body the response body
   * @return the sorted currency codes
   * @throws ExchangeRateUnavailableException if the body is not in the expected format
   */
  static List<String> parseCurrencies(String body) throws ExchangeRateUnavailableException {
    try {
      final List<String> codes = new ArrayList<>(new JSONObject(body).keySet());
      Collections.sort(codes);
      return codes;
    } catch (JSONException ex) {
      throw new ExchangeRateUnavailableException(
          "Unexpected response from exchange rate service", ex);
    }
  }
}
