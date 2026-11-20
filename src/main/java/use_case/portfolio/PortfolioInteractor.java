package use_case.portfolio;

import entity.Holding;
import entity.Portfolio;
import entity.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Coordinates portfolio operations between input, entities, and the stock price adapter.
 */
public final class PortfolioInteractor implements PortfolioInputBoundary {
    private final PortfolioOutputBoundary outputBoundary;
    private final StockPriceDataAccessInterface stockPriceDataAccess;
    private final PortfolioDataAccessInterface portfolioDataAccess;

    public PortfolioInteractor(
            PortfolioDataAccessInterface portfolioDataAccess,
            StockPriceDataAccessInterface stockPriceDataAccess,
            PortfolioOutputBoundary outputBoundary) {
        this.portfolioDataAccess = Objects.requireNonNull(
                portfolioDataAccess, "portfolioDataAccess must not be null");
        this.stockPriceDataAccess = Objects.requireNonNull(
                stockPriceDataAccess, "stockPriceDataAccess must not be null");
        this.outputBoundary = Objects.requireNonNull(
                outputBoundary, "outputBoundary must not be null");
    }

    @Override
    public void execute(PortfolioInputData inputData) {
        Objects.requireNonNull(inputData, "inputData must not be null");
        switch (inputData.getOperation()) {
            case BUY:
                executeBuy(inputData);
                break;
            case SELL:
                executeSell(inputData);
                break;
            case VIEW:
                executeView(inputData);
                break;
            case TRANSACTION_HISTORY:
                executeTransactionHistory(inputData);
                break;
            default:
                throw new AssertionError("Unexpected portfolio operation");
        }
    }

    private void executeBuy(PortfolioInputData inputData) {
        Portfolio portfolio = getPortfolio(inputData.getUsername());
        BigDecimal originalCash = portfolio.getAvailableCash();
        try {
            BigDecimal price = stockPriceDataAccess.getCurrentPrice(inputData.getTicker());
            portfolio.buy(inputData.getTicker(), inputData.getQuantity(), price);
        } catch (IOException | IllegalArgumentException exception) {
            outputBoundary.prepareBuyView(new TradeOutputData(
                    false, exception.getMessage(), portfolio.getAvailableCash()));
            return;
        }

        try {
            portfolioDataAccess.savePortfolio(inputData.getUsername(), portfolio);
        } catch (RuntimeException exception) {
            outputBoundary.prepareBuyView(new TradeOutputData(
                    false,
                    "Could not save your portfolio. Please try again.",
                    originalCash));
            return;
        }

        outputBoundary.prepareBuyView(new TradeOutputData(
                true,
                "Successfully bought " + inputData.getQuantity()
                        + " shares of " + inputData.getTicker() + ".",
                portfolio.getAvailableCash()));
    }

    private void executeSell(PortfolioInputData inputData) {
        Portfolio portfolio = getPortfolio(inputData.getUsername());
        BigDecimal originalCash = portfolio.getAvailableCash();
        try {
            BigDecimal price = stockPriceDataAccess.getCurrentPrice(inputData.getTicker());
            portfolio.sell(inputData.getTicker(), inputData.getQuantity(), price);
        } catch (IOException | IllegalArgumentException exception) {
            outputBoundary.prepareSellView(new TradeOutputData(
                    false, exception.getMessage(), portfolio.getAvailableCash()));
            return;
        }

        try {
            portfolioDataAccess.savePortfolio(inputData.getUsername(), portfolio);
        } catch (RuntimeException exception) {
            outputBoundary.prepareSellView(new TradeOutputData(
                    false,
                    "Could not save your portfolio. Please try again.",
                    originalCash));
            return;
        }

        outputBoundary.prepareSellView(new TradeOutputData(
                true,
                "Successfully sold " + inputData.getQuantity()
                        + " shares of " + inputData.getTicker() + ".",
                portfolio.getAvailableCash()));
    }

    private void executeView(PortfolioInputData inputData) {
        Portfolio portfolio = getPortfolio(inputData.getUsername());
        for (Holding holding : portfolio.getHoldings().values()) {
            try {
                BigDecimal price = stockPriceDataAccess.getCurrentPrice(holding.getTicker());
                portfolio.updateCurrentPrice(holding.getTicker(), price);
            } catch (IOException | IllegalArgumentException exception) {
                // Keep the last known price if a stock quote cannot be refreshed.
            }
        }
        try {
            portfolioDataAccess.savePortfolio(inputData.getUsername(), portfolio);
        } catch (RuntimeException exception) {
            System.err.println(
                    "Failed to save refreshed portfolio prices: "
                            + exception.getMessage());
        }

        List<HoldingOutputData> holdings = new ArrayList<>();
        for (Holding holding : portfolio.getHoldings().values()) {
            holdings.add(new HoldingOutputData(
                    holding.getTicker(),
                    holding.getQuantity(),
                    holding.getAveragePurchasePrice(),
                    holding.getCurrentPrice(),
                    holding.getMarketValue()));
        }

        outputBoundary.preparePortfolioView(new PortfolioViewOutputData(
                portfolio.getAvailableCash(),
                portfolio.getPortfolioValue(),
                portfolio.getProfitLoss(),
                holdings));
    }

    private void executeTransactionHistory(PortfolioInputData inputData) {
        Portfolio portfolio = getPortfolio(inputData.getUsername());
        List<TransactionOutputData> transactions = new ArrayList<>();
        for (Transaction transaction : portfolio.getTransactions()) {
            transactions.add(new TransactionOutputData(
                    transaction.getTicker(),
                    transaction.getQuantity(),
                    transaction.getPrice(),
                    transaction.getType(),
                    transaction.getTimestamp()));
        }
        outputBoundary.prepareTransactionHistoryView(
                new TransactionHistoryOutputData(transactions));
    }

    private Portfolio getPortfolio(String username) {
        Portfolio portfolio = portfolioDataAccess.loadPortfolio(username);
        return portfolio == null ? new Portfolio() : portfolio;
    }
}
