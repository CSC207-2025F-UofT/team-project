package controller;

import entity.Transaction;
import use_case.transaction_categorizer.TransactionCategorizerService;

import java.util.List;

public class TransactionCategorizerController {

    private final TransactionCategorizerService service;

    public TransactionCategorizerController(TransactionCategorizerService service) {
        this.service = service;
    }

    public void categorizeTransactions(List<Transaction> transactions) {
        service.categorizeTransactions(transactions);
    }
}
