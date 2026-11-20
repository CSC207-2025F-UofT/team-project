package data_access.portfolio;

import org.junit.jupiter.api.Test;
import stockprototype.StockDataSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NavigableMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StockPriceDataAccessAdapterTest {
    @Test
    public void returnsLatestDailyClosingPrice() throws IOException {
        StockDataSource source = ticker -> {
            NavigableMap<LocalDate, BigDecimal> prices = new TreeMap<>();
            prices.put(LocalDate.of(2026, 1, 1), new BigDecimal("100"));
            prices.put(LocalDate.of(2026, 1, 2), new BigDecimal("110"));
            return prices;
        };

        StockPriceDataAccessAdapter adapter = new StockPriceDataAccessAdapter(source);

        assertEquals(new BigDecimal("110"), adapter.getCurrentPrice("AAPL"));
    }

    @Test
    public void rejectsEmptyPriceHistory() {
        StockDataSource source = ticker -> new TreeMap<>();
        StockPriceDataAccessAdapter adapter = new StockPriceDataAccessAdapter(source);

        assertThrows(IOException.class, () -> adapter.getCurrentPrice("AAPL"));
    }
}
