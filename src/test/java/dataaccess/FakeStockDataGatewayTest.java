package dataaccess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import entity.Money;
import entity.Quote;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecase.StockDataUnavailableException;

class FakeStockDataGatewayTest {

  private FakeStockDataGateway gateway;

  @BeforeEach
  void setUp() {
    gateway = new FakeStockDataGateway();
  }

  @Test
  void getQuote_knownTicker_returnsRecordedQuote() throws StockDataUnavailableException {
    // Action
    Quote quote = gateway.getQuote("AAPL");

    // Assertion: values from AAPL-quote.json, recorded on 2026-10-31
    assertEquals("AAPL", quote.ticker());
    assertEquals(Money.of("344.63"), quote.price());
    assertEquals(Money.of("349.19"), quote.high());
    assertEquals(Money.of("334.35"), quote.low());
    assertEquals(Money.of("348.91"), quote.previousClose());
    assertEquals(Instant.ofEpochSecond(1793390400L), quote.time());
  }

  @Test
  void getQuote_priceWithThreeDecimals_roundsHalfEvenToCents()
      throws StockDataUnavailableException {
    // Action: the recorded open is 346.715
    Quote quote = gateway.getQuote("AAPL");

    // Assertion
    assertEquals(Money.of("346.72"), quote.open());
  }

  @Test
  void getQuote_everyRecordedTicker_returnsQuote() throws StockDataUnavailableException {
    for (String ticker : FakeStockDataGateway.TICKERS) {
      // Action
      Quote quote = gateway.getQuote(ticker);

      // Assertion
      assertEquals(ticker, quote.ticker());
    }
    assertEquals(5, FakeStockDataGateway.TICKERS.size());
  }

  @Test
  void getQuote_unknownTicker_throws() {
    // Action / Assertion
    assertThrows(StockDataUnavailableException.class, () -> gateway.getQuote("XYZQ"));
  }

  @Test
  void setPrice_knownTicker_changesPriceHighAndLow() throws StockDataUnavailableException {
    // Inputs
    Money newPrice = Money.of("300.00");

    // Action
    gateway.setPrice("AAPL", newPrice);

    // Assertion
    Quote quote = gateway.getQuote("AAPL");
    assertEquals(newPrice, quote.price());
    assertEquals(newPrice, quote.high());
    assertEquals(newPrice, quote.low());
  }

  @Test
  void setPrice_knownTicker_keepsOpenPreviousCloseAndTime()
      throws StockDataUnavailableException {
    // Inputs
    Quote before = gateway.getQuote("AAPL");

    // Action
    gateway.setPrice("AAPL", Money.of("300.00"));

    // Assertion
    Quote after = gateway.getQuote("AAPL");
    assertEquals(before.open(), after.open());
    assertEquals(before.previousClose(), after.previousClose());
    assertEquals(before.time(), after.time());
    assertEquals(Money.of("-48.91"), after.change());
  }

  @Test
  void setPrice_oneTicker_leavesOtherTickersUnchanged() throws StockDataUnavailableException {
    // Inputs
    Quote msftBefore = gateway.getQuote("MSFT");

    // Action
    gateway.setPrice("AAPL", Money.of("300.00"));

    // Assertion
    assertEquals(msftBefore, gateway.getQuote("MSFT"));
  }

  @Test
  void setPrice_unknownTicker_throws() {
    // Inputs
    Money price = Money.of("300.00");

    // Action / Assertion
    assertThrows(IllegalArgumentException.class, () -> gateway.setPrice("XYZQ", price));
  }

  @Test
  void setPrice_zeroPrice_throwsAndKeepsOldQuote() throws StockDataUnavailableException {
    // Inputs
    Quote before = gateway.getQuote("AAPL");

    // Action / Assertion
    assertThrows(IllegalArgumentException.class, () -> gateway.setPrice("AAPL", Money.ZERO));
    assertEquals(before, gateway.getQuote("AAPL"));
  }
}
