package use_case.track_portfolio;

import java.time.LocalDate;

/**
 * Input data for the TrackPortfolio use case.
 */
public class TrackPortfolioInputData {
    private final String portfolioId;
    private final String userId;
    private final LocalDate asOfDate;

    public TrackPortfolioInputData(String portfolioId, String userId, LocalDate asOfDate) {
        this.portfolioId = portfolioId;
        this.userId = userId;
        this.asOfDate = asOfDate;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getAsOfDate() {
        return asOfDate;
    }
}
