package stockprototype;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NavigableMap;

/** Prototype contract only; adapt to the team's agreed interface before integration. */
public interface StockDataSource {
    /** Raw daily closing prices, sorted oldest first. No currency conversion or adjustment. */
    NavigableMap<LocalDate, BigDecimal> getDailyClosingPrices(String symbol) throws IOException;
}
