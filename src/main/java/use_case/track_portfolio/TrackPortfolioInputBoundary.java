package use_case.track_portfolio;

/**
 * Input boundary for the TrackPortfolio use case.
 */
public interface TrackPortfolioInputBoundary {
    /**
     * Execute the track portfolio use case.
     * @param inputData Input data containing portfolio ID and user ID
     */
    void trackPortfolio(TrackPortfolioInputData inputData);
}
