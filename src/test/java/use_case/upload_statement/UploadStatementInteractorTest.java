package use_case.upload_statement;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import data_access.CSVTransactionDAO;
import entity.Transaction;
import interface_adapter.upload_statement.UploadStatementPresenter;

class UploadStatementInteractorTest {

    static class StubPresenter extends UploadStatementPresenter {

        private UploadStatementOutputData lastOutput;
        private boolean spendingLimitsCalled = false;

        public StubPresenter() {
            super(null, null, null, null, null); // we won't use ViewManager here
        }

        @Override
        public void prepareSuccessView(UploadStatementOutputData outputData) {
            this.lastOutput = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            fail("prepareFailView should not be called in this test. Got: " + errorMessage);
        }

        @Override
        public void prepareSpendingLimitsView() {
            this.spendingLimitsCalled = true;
        }
    }

    @Test
    void executeParsesCsvAndSavesTransactionsAndTotalSpend() throws Exception {
        File temp = File.createTempFile("upload", ".csv");
        temp.deleteOnExit();

        try (FileWriter fw = new FileWriter(temp)) {
            fw.write("date,category,amount,description\n");
            fw.write("2025-11-01,Food,45.50,Grocery store\n");
            fw.write("2025-11-05,Transport,35.00,Gas station\n");
        }

        CSVTransactionDAO dao = new CSVTransactionDAO("src/test/resources/csv/transactions.csv");
        StubPresenter presenter = new StubPresenter();

        UploadStatementInteractor interactor
                = new UploadStatementInteractor(dao, presenter);

        UploadStatementInputData inputData
                = new UploadStatementInputData(temp.getAbsolutePath(), "testUser");

        interactor.execute(inputData);

        List<Transaction> stored = dao.getAllTransactions();
        assertEquals(2, stored.size());
        assertEquals(80.50, presenter.lastOutput.getTotalSpend(), 0.0001);
        assertEquals("testUser", presenter.lastOutput.getUsername());
    }
}
