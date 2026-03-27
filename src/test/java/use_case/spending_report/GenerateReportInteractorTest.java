package use_case.spending_report;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.CSVTransactionDAO;
import entity.Transaction;

class GenerateReportInteractorTest {

    private TestPresenter presenter;
    private TestTransactionDAO transactionDAO;
    private GenerateReportInteractor interactor;

    @BeforeEach
    void setup() {
        presenter = new TestPresenter();
        transactionDAO = new TestTransactionDAO();
        interactor = new GenerateReportInteractor(transactionDAO, presenter);
    }

    @Test
    void testConstructor() {
        assertNotNull(interactor);
    }

    @Test
    void testGenerateReportSuccessWithTransactions() {
        Transaction t1 = new Transaction(LocalDate.now(), "Food", 50.0, "", "user123");
        Transaction t2 = new Transaction(LocalDate.now(), "Food", 30.0, "", "user123");
        Transaction t3 = new Transaction(LocalDate.now(), "Transport", 100.0, "", "user123");
        transactionDAO.mockTransactions.add(t1);
        transactionDAO.mockTransactions.add(t2);
        transactionDAO.mockTransactions.add(t3);

        interactor.execute(new GenerateReportInput("user123"));

        assertTrue(presenter.presentReportCalled);
        assertTrue(presenter.lastOutput.isSuccess());
        assertNotNull(presenter.lastOutput.getReport());

        Map<String, Float> categoryTotals = presenter.lastOutput.getReport().getCategoryBreakdown();
        assertEquals(80.0, categoryTotals.get("Food"), 0.01);
        assertEquals(100.0, categoryTotals.get("Transport"), 0.01);
    }

    @Test
    void testGenerateReportEmptyTransactions() {

        interactor.execute(new GenerateReportInput("user123"));

        assertTrue(presenter.presentReportCalled);
        assertFalse(presenter.lastOutput.isSuccess());
        assertNull(presenter.lastOutput.getReport());
    }

    @Test
    void testGenerateReportAggregatesByCategory() {
        transactionDAO.mockTransactions.add(new Transaction(LocalDate.now(), "Groceries", 25.0, "", "user123"));
        transactionDAO.mockTransactions.add(new Transaction(LocalDate.now(), "Groceries", 15.0, "", "user123"));
        transactionDAO.mockTransactions.add(new Transaction(LocalDate.now(), "Groceries", 50.0, "", "user123"));

        interactor.execute(new GenerateReportInput("user123"));

        assertTrue(presenter.presentReportCalled);
        assertTrue(presenter.lastOutput.isSuccess());
        Map<String, Float> categoryTotals = presenter.lastOutput.getReport().getCategoryBreakdown();
        assertEquals(90.0, categoryTotals.get("Groceries"), 0.01);
        assertEquals(1, categoryTotals.size());
    }

    @Test
    void testGenerateReportMultipleCategories() {
        transactionDAO.mockTransactions.add(new Transaction(LocalDate.now(), "Entertainment", 40.0, "", "user123"));
        transactionDAO.mockTransactions.add(new Transaction(LocalDate.now(), "Entertainment", 60.0, "", "user123"));
        transactionDAO.mockTransactions.add(new Transaction(LocalDate.now(), "Utilities", 20.0, "", "user123"));
        transactionDAO.mockTransactions.add(new Transaction(LocalDate.now(), "Utilities", 30.0, "", "user123"));

        interactor.execute(new GenerateReportInput("user123"));

        Map<String, Float> categoryTotals = presenter.lastOutput.getReport().getCategoryBreakdown();
        assertEquals(100.0, categoryTotals.get("Entertainment"), 0.01);
        assertEquals(50.0, categoryTotals.get("Utilities"), 0.01);
        assertEquals(2, categoryTotals.size());
    }

    @Test
    void testBackToCategorizeView() {
        interactor.backToCategorizeView();

        assertTrue(presenter.backToCategorizeViewCalled);
    }

    static class TestPresenter implements GenerateReportOutputBoundary {

        boolean presentReportCalled = false;
        boolean backToCategorizeViewCalled = false;
        GenerateReportOutput lastOutput;

        @Override
        public void presentReport(GenerateReportOutput outputData) {
            presentReportCalled = true;
            lastOutput = outputData;
        }

        @Override
        public void backToCategorizeView() {
            backToCategorizeViewCalled = true;
        }
    }

    static class TestTransactionDAO extends CSVTransactionDAO {

        List<Transaction> mockTransactions = new ArrayList<>();

        public TestTransactionDAO() {
            super("transactions.csv");
        }

        @Override
        public List<Transaction> getTransactions(String username) {
            return mockTransactions;
        }
    }
}
