package use_case.track_portfolio;

import java.time.LocalDateTime;

import entity.Position;

/**
 * Output data for the TrackPortfolio use case.
 */
public class TrackPortfolioOutputData {
    private final String portfolioId;
    private final Position[] positions;
    private final double totalRealizedGain;
    private final double totalUnrealizedGain;
    private final LocalDateTime snapshotTime;

    public TrackPortfolioOutputData(String portfolioId, Position[] positions, 
                                    double totalRealizedGain, double totalUnrealizedGain,
                                    LocalDateTime snapshotTime) {
        this.portfolioId = portfolioId;
        this.positions = positions;
        this.totalRealizedGain = totalRealizedGain;
        this.totalUnrealizedGain = totalUnrealizedGain;
        this.snapshotTime = snapshotTime;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public Position[] getPositions() {
        return positions;
    }

    public double getTotalRealizedGain() {
        return totalRealizedGain;
    }

    public double getTotalUnrealizedGain() {
        return totalUnrealizedGain;
    }

    public LocalDateTime getSnapshotTime() {
        return snapshotTime;
    }
}
