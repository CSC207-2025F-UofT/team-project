package data_access;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stockprototype.StockDataSource;

import java.io.IOError;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class WatchListStockDataAccessObjectTest {

    private static class FakeStockDataSource implements StockDataSource {
        private final NavigableMap<LocalDate, BigDecimal> prices = new TreeMap<>();
        private String requestedSymbol;
        private int timesCaleed = 0;
        private IOException failure;

        void addPrices(int day, String price){
            prices.put(LocalDate.of(2026, 11, day), new BigDecimal(price));
        }

        @Override
        public NavigableMap<LocalDate, BigDecimal> getDailyClosingPrices(String symbol)
                throws IOException{
            requestedSymbol = symbol;
            timesCaleed++;
            if (failure != null){
                throw failure;
            }
            return prices;
        }

        private FakeStockDataSource source;
        private WatchListStockDataAccessObject dao;

        @BeforeEach
        void setUp(){
            source = new FakeStockDataSource();
            dao = new WatchListStockDataAccessObject(source);
        }

        @Test
        void askTheDataSourceForStockOnce() throws  IOException{
            source.addPrices(1, "100");
            dao.getStockPrice("MSFT", 2);

            assertEquals("MSFT", source.requestedSymbol);
            assertEquals(1, source.timesCaleed);
        }

        @Test
        void convertsBigDecimalPriceToDouble() throws IOException{
            source.addPrices(1, "123.45");

            List<Double> result = dao.getStockPrice("IBM", 1);
            assertEquals(List.of(123.45), result);
        }

        @Test
        void returnsOnlyLatestPricesOldFirst() throws IOException{
            source.addPrices(5, "50");
            source.addPrices(2, "20");
            source.addPrices(4, "40");
            source.addPrices(1, "10");
            source.addPrices(3, "30");

            List<Double> result = dao.getStockPrice("IBM", 2);
            assertEquals(List.of(40.0, 50.0), result);
        }

        @Test
        void returnsAllWhenCountIsLargerThanList() throws  IOException{
            source.addPrices(1, "10");
            source.addPrices(2, "20");

            List<Double> result = dao.getStockPrice("IBM", 5);
            assertEquals(List.of(10.0, 20.0), result);
        }

        @Test
        void returnsEmptyListWhenNoPriceAvailable() throws  IOException{
            assertTrue(dao.getStockPrice("IBM", 2).isEmpty());
        }

        @Test
        void returnsEmptyListWhenListEmpty() throws IOException{
            source.addPrices(1, "10");

            assertTrue(dao.getStockPrice("IBM", 0).isEmpty());
        }

        @Test
        void communicatesDataSourceError(){
            source.failure = new IOException("rate limit reached");

            IOException error = assertThrows(IOException.class, () -> dao.getStockPrice("IBM", 2));
            assertEquals("rate limit reached", error.getMessage());
        }

        @Test
        void negativeCountReturnsEmptyList() throws IOException{
            source.addPrices(1, "10");

            assertTrue(dao.getStockPrice("IBM", -1).isEmpty());
        }

        @Test
        void emptyCountDoesNotCallDataSource() throws IOException{
            source.addPrices(1, "10");

            dao.getStockPrice("IBM", 1);
            assertEquals(0, source.timesCaleed);
        }
    }


}
