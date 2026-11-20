package interface_adapter.portfolio;

import use_case.portfolio.PortfolioInputBoundary;
import use_case.portfolio.PortfolioInputData;

import java.util.Objects;

/**
 * Handles user requests for portfolio operations.
 */
public final class PortfolioController {

    private final PortfolioInputBoundary portfolioInputBoundary;

    public PortfolioController(PortfolioInputBoundary portfolioInputBoundary) {
        this.portfolioInputBoundary = Objects.requireNonNull(
                portfolioInputBoundary,
                "portfolioInputBoundary must not be null");
    }

    public void buy(String username, String ticker, int quantity) {
        portfolioInputBoundary.execute(new PortfolioInputData(
                username,
                ticker,
                quantity,
                PortfolioInputData.Operation.BUY));
    }

    public void sell(String username, String ticker, int quantity) {
        portfolioInputBoundary.execute(new PortfolioInputData(
                username,
                ticker,
                quantity,
                PortfolioInputData.Operation.SELL));
    }

    public void viewPortfolio(String username) {
        portfolioInputBoundary.execute(new PortfolioInputData(
                username,
                PortfolioInputData.Operation.VIEW));
    }

    public void viewTransactionHistory(String username) {
        portfolioInputBoundary.execute(new PortfolioInputData(
                username,
                PortfolioInputData.Operation.TRANSACTION_HISTORY));
    }
}
