package use_case.upload_statement;

import data_access.InMemoryTransactionDataAccessObject;
import entity.Transaction;
import interface_adapter.upload_statement.UploadStatementPresenter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UploadStatementInteractor implements UploadStatementInputBoundary {
    private final InMemoryTransactionDataAccessObject transactionGateway;
    private final UploadStatementPresenter uploadStatementPresenter;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public UploadStatementInteractor(InMemoryTransactionDataAccessObject transactionGateway,
                                     UploadStatementPresenter uploadStatementPresenter) {
        this.transactionGateway = transactionGateway;
        this.uploadStatementPresenter = uploadStatementPresenter;
    }

    @Override
    public void execute(UploadStatementInputData inputData) {

        String path = inputData.getCsvFilePath();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                LocalDate date = LocalDate.parse(values[0], formatter);
                String title = values[1];
                double amount = Double.parseDouble(values[2]);

                Transaction transaction = new Transaction(date, title, amount);

                transactionGateway.saveTransaction(transaction);
            }
            List<Transaction> transactionList = transactionGateway.getAllTransactions();
            double totalSpend = transactionList.stream().mapToDouble(Transaction::getAmount).sum();
            UploadStatementOutputData outputData = new UploadStatementOutputData(transactionList.size(),
                    true, "Successfully processed your statement", new HashMap<String, Double>(),
                    new ArrayList(), totalSpend);
            uploadStatementPresenter.prepareSuccessView(outputData);
        } catch (IOException e) {
            throw new RuntimeException("Invalid file or unable to read CSV");
        }
    }
}
