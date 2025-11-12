package interface_adapter.controller;

import java.time.LocalDate;

import use_case.track_portfolio.TrackPortfolioInputBoundary;
import use_case.track_portfolio.TrackPortfolioInputData;

/**
 * Controller for trading/portfolio operations.
 * Handles user input from the UI and delegates to use cases.
 */
public class TradingController {
    private final TrackPortfolioInputBoundary inputBoundary;

    public TradingController(TrackPortfolioInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    /**
     * Handle request to view portfolio details.
     * @param portfolioId Portfolio ID to view
     * @param userId User ID requesting the portfolio
     */
    public void viewPortfolio(String portfolioId, String userId) {
        // TODO: Add input validation
        if (portfolioId == null || portfolioId.isEmpty()) {
            throw new IllegalArgumentException("Portfolio ID cannot be empty");
        }
        
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }

        // Create input data and call use case
        TrackPortfolioInputData inputData = new TrackPortfolioInputData(
                portfolioId, 
                userId, 
                LocalDate.now()
        );
        
        inputBoundary.trackPortfolio(inputData);
    }

    /**
     * TODO: Add method to handle portfolio refresh with specific date
     * TODO: Add methods for other trading operations (buy, sell)
     * TODO: Add method to create new portfolio
     * TODO: Add method to add funds to portfolio
     */
    
    public void refreshPortfolio(String portfolioId, String userId, LocalDate asOfDate) {
        TrackPortfolioInputData inputData = new TrackPortfolioInputData(
                portfolioId, 
                userId, 
                asOfDate
        );
        inputBoundary.trackPortfolio(inputData);
    }
}
