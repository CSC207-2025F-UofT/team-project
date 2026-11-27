package interface_adapter;

import interface_adapter.watchlist.WatchListViewModel;
import interface_adapter.watchlist.WatchlistPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.watchlist.WatchListOutputdata;
import use_case.watchlist.WatchlistOutputBoundary;
import view.WatchlistView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WatchlistPresenterTEst {
    private WatchListViewModel viewModel;
    private WatchlistPresenter presenter;

    @BeforeEach
    void setUp(){
        viewModel = new WatchListViewModel();
        presenter = new WatchlistPresenter(viewModel);
    }

    private WatchListOutputdata refreshData(){
        List<WatchListOutputdata.Change> changes = new ArrayList<>();
        changes.add(WatchListOutputdata.Change.success("IBM", 110.0, 10.0));
        changes.add(WatchListOutputdata.Change.failure("AAPL", 0, 0 , "no data"));
        return new WatchListOutputdata(List.of("IBM", "AAPL"), changes);
    }

    @Test
    void refreshShowsOneLinePerStock(){
        presenter.prepareSuccessView(refreshData());

        assertEquals("IBM: 110.00 (+10.00%)\nAAPL: no data\n", viewModel.getChangesText());
        assertEquals(List.of("IBM", "AAPL"), viewModel.getStockWatchers());
        assertNull(viewModel.getErrorMessage());
    }

    @Test
    void removingAStockRemovesItsPriceLine(){
        presenter.prepareSuccessView(refreshData());

        presenter.prepareSuccessView(new WatchListOutputdata(List.of("AAPL"), new ArrayList<>()));
        assertEquals("AAPL: no data\n", viewModel.getChangesText());
    }

    @Test
    void addingAStockKeepsPriceLines(){
        presenter.prepareSuccessView(refreshData());

        presenter.prepareSuccessView(new WatchListOutputdata(List.of("IBM", "AAPL", "MSFT"), new ArrayList<>()));
        assertEquals("IBM: 110.00 (+10.00%)\nAAPL: no data\n", viewModel.getChangesText());
    }

    @Test
    void emptyWatchListClearsPrice(){
        presenter.prepareSuccessView(refreshData());

        presenter.prepareSuccessView(new WatchListOutputdata(new ArrayList<>(), new ArrayList<>()));
        assertEquals("", viewModel.getChangesText());
    }

    @Test
    void secondRefreshReplacesOldText(){
        presenter.prepareSuccessView(refreshData());

        List<WatchListOutputdata.Change> changes = new ArrayList<>();
        changes.add(WatchListOutputdata.Change.success("IBM", 99.0, -1.0));
        presenter.prepareSuccessView(new WatchListOutputdata(List.of("IBM"), changes));
        assertEquals("IBM: 99.00 (-1.00%)\n", viewModel.getChangesText());
    }

    @Test
    void failViewSetsMessageKeepsList(){
        presenter.prepareSuccessView(refreshData());

        presenter.prepareFailView("Could not connect to User API");

        assertEquals("Could not connect to User API", viewModel.getErrorMessage());
        assertEquals(List.of("IBM", "AAPL"), viewModel.getStockWatchers());
    }

    @Test
    void successClearsPreviousError(){
        presenter.prepareFailView("fail");
        presenter.prepareSuccessView(refreshData());
        assertNull(viewModel.getErrorMessage());
    }
}
