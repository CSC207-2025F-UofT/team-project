package use_case.portfolio;

/**
 * Presents the results of portfolio use-case operations.
 */
public interface PortfolioOutputBoundary {
    void prepareBuyView(TradeOutputData outputData);

    void prepareSellView(TradeOutputData outputData);

    void preparePortfolioView(PortfolioViewOutputData outputData);

    void prepareTransactionHistoryView(TransactionHistoryOutputData outputData);
}
