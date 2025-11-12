package interface_adapter.view_model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * View model for portfolio display in the UI.
 */
public class PortfolioViewModel {
    private final String portfolioId;
    private final PositionView[] positions;
    private final double totalRealizedGain;
    private final double totalUnrealizedGain;
    private final LocalDateTime snapshotTime;

    public PortfolioViewModel(String portfolioId, PositionView[] positions,
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

    public PositionView[] getPositions() {
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

    // TODO: Add convenience methods for formatted display
    public String getFormattedRealizedGain() {
        return String.format("$%.2f", totalRealizedGain);
    }

    public String getFormattedUnrealizedGain() {
        return String.format("$%.2f", totalUnrealizedGain);
    }

    public String getFormattedSnapshotTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return snapshotTime.format(formatter);
    }

    public double getTotalGain() {
        return totalRealizedGain + totalUnrealizedGain;
    }
}
