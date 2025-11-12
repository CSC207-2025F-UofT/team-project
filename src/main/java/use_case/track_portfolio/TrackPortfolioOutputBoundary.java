package use_case.track_portfolio;

/**
 * Output boundary for the TrackPortfolio use case.
 */
public interface TrackPortfolioOutputBoundary {
    /**
     * Present the portfolio data to the user.
     * @param outputData Output data containing portfolio positions and gains
     */
    void presentPortfolio(TrackPortfolioOutputData outputData);

    /**
     * Present an error message when portfolio cannot be tracked.
     * @param error Error message
     */
    void presentError(String error);
}
