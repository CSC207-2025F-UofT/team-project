package use_case.filter_search;

import data_access.CSVTransactionDAO;
import entity.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class FilterSearchInteractor implements FilterSearchInputBoundary {

    private final CSVTransactionDAO transactionDAO;
    private final FilterSearchOutputBoundary presenter;

    public FilterSearchInteractor(CSVTransactionDAO transactionDAO,
                                  FilterSearchOutputBoundary presenter) {
        this.transactionDAO = transactionDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(FilterSearchInputData inputData) {

        // Validation: invalid date range
        if (inputData.getStartDate() != null &&
                inputData.getEndDate() != null &&
                inputData.getStartDate().isAfter(inputData.getEndDate())) {

            presenter.prepareFailView("Start date cannot be after end date.");
            return;
        }

        List<Transaction> all = transactionDAO.getAllTransactions();

        List<Transaction> filtered = all;

        // Category filter
        if (inputData.getCategory() != null && !inputData.getCategory().equals("All")) {
            filtered = filtered.stream()
                    .filter(t -> inputData.getCategory().equalsIgnoreCase(t.getCategory()))
                    .collect(Collectors.toList());
        }

        // Start date
        if (inputData.getStartDate() != null) {
            filtered = filtered.stream()
                    .filter(t -> !t.getDate().isBefore(inputData.getStartDate()))
                    .collect(Collectors.toList());
        }

        // End date
        if (inputData.getEndDate() != null) {
            filtered = filtered.stream()
                    .filter(t -> !t.getDate().isAfter(inputData.getEndDate()))
                    .collect(Collectors.toList());
        }

        // Merchant keyword
        if (inputData.getMerchant() != null &&
                !inputData.getMerchant().trim().isEmpty()) {

            String keyword = inputData.getMerchant().toLowerCase();

            filtered = filtered.stream()
                    .filter(t -> t.getMerchant().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
        }

        presenter.prepareSuccessView(new FilterSearchOutputData(filtered));
    }
}
