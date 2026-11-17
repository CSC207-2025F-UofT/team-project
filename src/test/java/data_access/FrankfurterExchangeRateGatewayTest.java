package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import use_case.exchange_rate.ExchangeRateUnavailableException;

/**
 * Tests the parts of the gateway that do not need the internet. Run {@code app.ExchangeRateApiDemo}
 * to check the live API.
 */
class FrankfurterExchangeRateGatewayTest {

  @Test
  void sameCurrencyNeedsNoApiCall() throws ExchangeRateUnavailableException {
    // An unreachable URL proves no request is made.
    final FrankfurterExchangeRateGateway gateway =
        new FrankfurterExchangeRateGateway("http://localhost:1");
    assertEquals(1.0, gateway.getRate("cad", "CAD"));
  }

  @Test
  void parsesRateFromRealResponseShape() throws ExchangeRateUnavailableException {
    final String body =
        "{\"amount\":1.0,\"base\":\"CAD\",\"date\":\"2026-11-16\",\"rates\":{\"JPY\":111.72}}";
    assertEquals(111.72, FrankfurterExchangeRateGateway.parseRate(body, "JPY"));
  }

  @Test
  void unexpectedResponseIsReported() {
    assertThrows(
        ExchangeRateUnavailableException.class,
        () -> FrankfurterExchangeRateGateway.parseRate("{\"message\":\"not found\"}", "JPY"));
    assertThrows(
        ExchangeRateUnavailableException.class,
        () -> FrankfurterExchangeRateGateway.parseCurrencies("not json"));
  }

  @Test
  void parsesSortedCurrencyList() throws ExchangeRateUnavailableException {
    final String body = "{\"JPY\":\"Japanese Yen\",\"CAD\":\"Canadian Dollar\",\"EUR\":\"Euro\"}";
    assertEquals(
        List.of("CAD", "EUR", "JPY"), FrankfurterExchangeRateGateway.parseCurrencies(body));
  }

  @Test
  void unreachableServerIsReported() {
    final FrankfurterExchangeRateGateway gateway =
        new FrankfurterExchangeRateGateway("http://localhost:1");
    assertThrows(ExchangeRateUnavailableException.class, () -> gateway.getRate("CAD", "JPY"));
  }
}
