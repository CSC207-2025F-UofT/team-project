package stockprototype.comparison;

import org.junit.jupiter.api.Test;
import stockprototype.StockDataSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class ComparisonInteractorTest {
    private static final LocalDate START = LocalDate.of(2026, 11, 9);

    @Test
    void intersectsBeforeLimitingAndRebasesOnFirstDateInPeriod() throws IOException {
        NavigableMap<LocalDate, BigDecimal> first = prices("50", "200", null, "210", "220", "999");
        NavigableMap<LocalDate, BigDecimal> second = prices("60", "100", "105", "110", "115");
        List<String> calls = new ArrayList<>();
        ComparisonInteractor interactor = new ComparisonInteractor(symbol -> {
            calls.add(symbol);
            return symbol.equals("IBM") ? first.descendingMap() : second;
        });
        ComparisonResult result = interactor.compare(" ibm ", "aapl", 3);

        assertEquals(List.of("IBM", "AAPL"), calls);
        assertEquals("IBM", result.firstSymbol);
        assertEquals("AAPL", result.secondSymbol);
        assertEquals(3, result.days.size());
        assertEquals(START.plusDays(1), result.days.get(0).date);
        assertEquals(START.plusDays(3), result.days.get(1).date);
        assertEquals(START.plusDays(4), result.days.get(2).date);
        assertDecimal("105", result.days.get(1).firstIndex);
        assertDecimal("110", result.days.get(1).secondIndex);
        assertDecimal("10", result.firstChange);
        assertDecimal("15", result.secondChange);
        assertThrows(UnsupportedOperationException.class, () -> result.days.clear());
    }

    @Test
    void projectsOneIntervalFromGeometricGrowth() throws IOException {
        ComparisonResult result = compare(prices("100", "126", "121"), prices("200", "170", "162"));
        assertDecimal("133.1", result.firstProjection);
        assertDecimal("145.8", result.secondProjection);
        assertDecimal("-19", result.secondChange);
    }

    @Test
    void correlationUsesChangesBetweenIdenticalEndpoints() throws IOException {
        // Aligned returns are +10%, -10%, 0% for the first and -10%, +10%, 0% for the second.
        ComparisonResult result = compare(prices("100", "110", null, "99", "99"),
                prices("200", "180", "500", "198", "198"));
        assertEquals(-1.0, result.correlation.orElseThrow(), 1e-12);
    }

    @Test
    void handlesEqualCorrelationsAndUndefinedCases() throws IOException {
        ComparisonResult same = compare(prices("1", "2", "3", "5"), prices("2", "4", "6", "10"));
        assertEquals(1.0, same.correlation.orElseThrow(), 1e-12);
        assertTrue(compare(prices("63", "63", "63"), prices("3", "4", "6")).correlation.isEmpty());
        assertTrue(compare(prices("100", "110", "121"), prices("3", "4", "6")).correlation.isEmpty());
        ComparisonResult shortPeriod = compare(prices("100", "110"), prices("200", "160"));
        assertTrue(shortPeriod.correlation.isEmpty());
        assertDecimal("121", shortPeriod.firstProjection);
    }

    @Test
    void validatesBothSymbolsAndWindowBeforeAnyRequest() {
        ComparisonInteractor interactor = new ComparisonInteractor(symbol -> fail("Unexpected fetch: " + symbol));
        assertThrows(IllegalArgumentException.class, () -> interactor.compare("AAPL", " aapl ", 0));
        assertThrows(IllegalArgumentException.class, () -> interactor.compare("AAPL", "bad key", 0));
        assertThrows(IllegalArgumentException.class, () -> interactor.compare(null, "IBM", 0));
        assertThrows(IllegalArgumentException.class, () -> interactor.compare("AAPL", "IBM", 1));
        assertThrows(IllegalArgumentException.class, () -> interactor.compare("AAPL", "IBM", -5));
    }

    @Test
    void rejectsUnusableDataAndPropagatesServiceErrors() {
        assertThrows(IllegalArgumentException.class, () -> compare(prices("22", null), prices("1", "2")));
        assertThrows(IllegalArgumentException.class, () -> compare(prices("22", "0"), prices("1", "2")));
        ComparisonInteractor interactor = new ComparisonInteractor(symbol -> {
            if (symbol.equals("AAPL")) {
                throw new IOException("Price service unavailable.");
            }
            return prices("100", "110");
        });
        assertThrows(IOException.class, () -> interactor.compare("IBM", "AAPL", 0));
    }

    private static ComparisonResult compare(NavigableMap<LocalDate, BigDecimal> a,
                                            NavigableMap<LocalDate, BigDecimal> b) throws IOException {
        StockDataSource source = symbol -> symbol.equals("A") ? a : b;
        return new ComparisonInteractor(source).compare("A", "B", 0);
    }

    private static NavigableMap<LocalDate, BigDecimal> prices(String... closes) {
        NavigableMap<LocalDate, BigDecimal> prices = new TreeMap<>();
        for (int i = 0; i < closes.length; i++) {
            if (closes[i] != null) {
                prices.put(START.plusDays(i), new BigDecimal(closes[i]));
            }
        }
        return prices;
    }

    private static void assertDecimal(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual), "Actual: " + actual);
    }
}
