package use_case.watchlist;

import entity.Watchlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WatchListInteractorTest {

    private static class FakeWatchListDataAccess implements  WatchListDataAccessInterface{
        private final Map<String, Watchlist> data = new HashMap<>();
        private boolean failing = false;

        @Override
        public Watchlist get(String username){
            if (failing){
                throw new RuntimeException("Could not connect to the user API");
            }
            return data.getOrDefault(username, new Watchlist());
        }

        @Override
        public void save(String username, Watchlist watchList){
            if(failing){
                throw new RuntimeException("Couldn't connect to the user API");
            }
            data.put(username, watchList);
        }
    }

    private static  class FakeStockDataAccess implements  WatchlistStockDataAccessInterface{
        @Override
        public List<Double> getStockPrice(String stockWatcher, int count) throws IOException {
            switch (stockWatcher) {
                case "IBM":
                    return List.of(100.0, 110.0);
                case "SHORT":
                    return List.of(100.0);
                case "NULLP":
                    return Arrays.asList(100.0, null);
                case "NEG":
                    return List.of(100.0, -5.0);
                case "ZERO":
                    return List.of(0.0, 10.0);
                case "NAN":
                    return List.of(100.0, Double.NaN);
                default:
                    throw new IOException("no data");
            }
        }
    }

    private static class FakePresenter implements WatchlistOutputBoundary{
        private WatchListOutputdata outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(WatchListOutputdata outputdata){
            this.outputData = outputdata;
        }

        @Override
        public void prepareFailView(String errorMessage){
            this.errorMessage = errorMessage;
        }
    }

    private FakeWatchListDataAccess dataAccess;
    private FakePresenter presenter;
    private WatchListInteractor interactor;

    @BeforeEach
    void setUp(){
        presenter = new FakePresenter();
        dataAccess = new FakeWatchListDataAccess();
        interactor = new WatchListInteractor(dataAccess, new FakeStockDataAccess(), presenter);
    }

    @Test
    void addStockSavesUppercaseTicker() {
        interactor.addStock(new WatchlistInputData("sam", " ibm "));

        assertEquals(List.of("IBM"), presenter.outputData.getStockWatchers());
        assertNull(presenter.errorMessage);
    }

    @Test
    void addDuplicateStockFails() {
        interactor.addStock(new WatchlistInputData("sam", "IBM"));
        interactor.addStock(new WatchlistInputData("sam", "ibm"));

        assertEquals("IBM is already on the watchlist",
                presenter.errorMessage);
    }

    @Test
    void addBlankStockFails() {
        interactor.addStock(new WatchlistInputData("sam", "  "));

        assertEquals("Cannot leave entry blank", presenter.errorMessage);
    }

    @Test
    void removeStockRemovesIt() {
        interactor.addStock(new WatchlistInputData("sam", "IBM"));
        interactor.addStock(new WatchlistInputData("sam", "AAPL"));
        interactor.removeStock(new WatchlistInputData("sam", "IBM"));

        assertEquals(List.of("AAPL"), presenter.outputData.getStockWatchers());
    }

    @Test
    void watchlistsAreSeparatePerUser() {
        interactor.addStock(new WatchlistInputData("sam", "IBM"));
        interactor.showWatchlist(new WatchlistInputData("alex"));

        assertTrue(presenter.outputData.getStockWatchers().isEmpty());
    }

    @Test
    void refreshChangesCalculatesPercentChange() {
        interactor.addStock(new WatchlistInputData("sam", "IBM"));
        interactor.refreshChange(new WatchlistInputData("sam"));

        WatchListOutputdata.Change change = presenter.outputData.getChanges().get(0);
        assertEquals("IBM", change.getStockWatcher());
        assertEquals(110.0, change.getLatestPrice());
        assertEquals(10.0, change.getPercentChange(), 0.0001);
    }

    @Test
    void refreshChangesReportsStocksWithoutData() {
        interactor.addStock(new WatchlistInputData("sam", "XYZ"));
        interactor.refreshChange(new WatchlistInputData("sam"));

        WatchListOutputdata.Change change = presenter.outputData.getChanges().get(0);
        assertTrue(change.hasError());
        assertEquals("no data", change.getErrorMessage());
    }

    @Test
    void removeStockIgnoresCaseAndSpaces() {
        interactor.addStock(new WatchlistInputData("sam", "AAPL"));
        interactor.removeStock(new WatchlistInputData("sam", " aapl "));

        assertTrue(presenter.outputData.getStockWatchers().isEmpty());
        assertNull(presenter.errorMessage);
    }

    @Test
    void removeBlankStockFails() {
        interactor.removeStock(new WatchlistInputData("sam", " "));

        assertEquals("No stock selected", presenter.errorMessage);
    }

    @Test
    void removeStockNotInWatchlistFails() {
        interactor.addStock(new WatchlistInputData("sam", "IBM"));
        interactor.removeStock(new WatchlistInputData("sam", "AAPL"));

        assertEquals("AAPL is not on the watchlist", presenter.errorMessage);
    }

    @Test
    void refreshChangesRejectsInvalidPrices() {
        for (String ticker : List.of("SHORT", "NULLP", "NEG", "ZERO", "NAN")) {
            interactor.addStock(new WatchlistInputData("sam", ticker));
        }
        interactor.refreshChange(new WatchlistInputData("sam"));

        List<WatchListOutputdata.Change> changes =
                presenter.outputData.getChanges();
        assertEquals(5, changes.size());
        for (WatchListOutputdata.Change change : changes) {
            assertTrue(change.hasError());
        }
        assertEquals("Not enough data available from API", changes.get(0).getErrorMessage());
        assertEquals("invalid price data", changes.get(1).getErrorMessage());
    }

    @Test
    void storageFailureIsReportedNotThrown() {
        dataAccess.failing = true;

        interactor.showWatchlist(new WatchlistInputData("sam"));
        assertEquals("Could not connect to the user API",
                presenter.errorMessage);

        presenter.errorMessage = null;
        interactor.addStock(new WatchlistInputData("sam", "IBM"));
        assertEquals("Could not connect to the user API",
                presenter.errorMessage);

        presenter.errorMessage = null;
        interactor.removeStock(new WatchlistInputData("sam", "IBM"));
        assertEquals("Could not connect to the user API",
                presenter.errorMessage);

        presenter.errorMessage = null;
        interactor.refreshChange(new WatchlistInputData("sam"));
        assertEquals("Could not connect to the user API",
                presenter.errorMessage);
    }
}
