package use_case.filter_search;

import data_access.CSVTransactionDAO;
import entity.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterSearchInteractorTest {

    // ---- Fake DAO (no Mockito) ----
    static class FakeDAO extends CSVTransactionDAO {
        private final List<Transaction> data;

        public FakeDAO(List<Transaction> data) {
            super("fake.csv"); // not used
            this.data = data;
        }

        @Override
        public List<Transaction> getAllTransactions() {
            return data;
        }
    }

    // ---- Fake Presenter to capture output ----
    static class FakePresenter implements FilterSearchOutputBoundary {

        FilterSearchOutputData lastSuccess;
        String lastError;

        @Override
        public void prepareSuccessView(FilterSearchOutputData outputData) {
            lastSuccess = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            lastError = errorMessage;
        }
    }

    private List<Transaction> sampleData() {
        List<Transaction> list = new ArrayList<>();

        list.add(new Transaction(
                LocalDate.of(2024, 1, 1), "GROCERIES", 20.0, "Walmart", "u")
        );

        list.add(new Transaction(
                LocalDate.of(2024, 1, 15), "SHOPPING", 55.0, "Amazon", "u")
        );

        return list;
    }

    // ----------------------------------------------------
    // TEST 1 — No filters returns ALL transactions
    // ----------------------------------------------------
    @Test
    void testNoFiltersReturnsAll() {
        FakeDAO dao = new FakeDAO(sampleData());
        FakePresenter presenter = new FakePresenter();

        FilterSearchInteractor interactor = new FilterSearchInteractor(dao, presenter);

        FilterSearchInputData input = new FilterSearchInputData(
                "All", null, null, ""
        );

        interactor.execute(input);

        assertNotNull(presenter.lastSuccess);
        assertEquals(2, presenter.lastSuccess.getFilteredTransactions().size());
    }

    // ----------------------------------------------------
    // TEST 2 — Category filtering
    // ----------------------------------------------------
    @Test
    void testCategoryFilter() {
        FakeDAO dao = new FakeDAO(sampleData());
        FakePresenter presenter = new FakePresenter();

        FilterSearchInteractor interactor = new FilterSearchInteractor(dao, presenter);

        FilterSearchInputData input = new FilterSearchInputData(
                "SHOPPING", null, null, ""
        );

        interactor.execute(input);

        assertNotNull(presenter.lastSuccess);
        assertEquals(1, presenter.lastSuccess.getFilteredTransactions().size());
        assertEquals("SHOPPING",
                presenter.lastSuccess.getFilteredTransactions().get(0).getCategory());
    }

    // ----------------------------------------------------
    // TEST 3 — Invalid date range
    // ----------------------------------------------------
    @Test
    void testInvalidDateRange() {
        FakeDAO dao = new FakeDAO(sampleData());
        FakePresenter presenter = new FakePresenter();

        FilterSearchInteractor interactor = new FilterSearchInteractor(dao, presenter);

        FilterSearchInputData input = new FilterSearchInputData(
                "All",
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 1, 1),
                ""
        );

        interactor.execute(input);

        assertNull(presenter.lastSuccess);
        assertEquals("Start date cannot be after end date.", presenter.lastError);
    }
}
