package use_case.upload_statement;

import entity.Transaction;
import interface_adapter.TransactionDataAccess;
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
    private final TransactionDataAccess transactionGateway;
    private final UploadStatementOutputBoundary uploadStatementPresenter;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public UploadStatementInteractor(TransactionDataAccess transactionGateway,
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
                String[] values = line.split(",",4);

                LocalDate date = LocalDate.parse(values[0].trim(), formatter);
                String description = values[1].trim();
                double amount = Double.parseDouble(values[2].trim());

                Transaction transaction = new Transaction(date, "", amount, description,
                        inputData.getUsername());

                transactionGateway.saveTransaction(transaction);
            }
            List<Transaction> transactionList = transactionGateway.getTransactions(inputData.getUsername());
            double totalSpend = transactionList.stream().mapToDouble(Transaction::getAmount).sum();
            UploadStatementOutputData outputData = new UploadStatementOutputData(transactionList.size(),
                    true, "Successfully processed your statement", new HashMap<String, Double>(),
                    new ArrayList(), totalSpend, inputData.getUsername());
            uploadStatementPresenter.prepareSuccessView(outputData);
        } catch (IOException e) {
            throw new RuntimeException("Invalid file or unable to read CSV");
        }
    }
    @Override
    public void goToSpendingLimits() {
        uploadStatementPresenter.prepareSpendingLimitsView();
    }

    @Override
    public void goToSpendingReport() {
        uploadStatementPresenter.prepareSpendingReportView();
    }


    @Override
    public void goToCategorizer(UploadStatementInputData inputData) {
        uploadStatementPresenter.prepareCategorizerView(transactionGateway.getTransactions(inputData.getUsername()));
    }
    public void goToFilterSearch() {
        uploadStatementPresenter.prepareFilterSearchView();
    }
}
