package use_case.portfolio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Presentation data for the current portfolio.
 */
public final class PortfolioViewOutputData {
    private final BigDecimal availableCash;
    private final BigDecimal portfolioValue;
    private final BigDecimal profitLoss;
    private final List<HoldingOutputData> holdings;

    public PortfolioViewOutputData(BigDecimal availableCash, BigDecimal portfolioValue,
                                   BigDecimal profitLoss,
                                   List<HoldingOutputData> holdings) {
        this.availableCash = requireNonNegative(availableCash, "availableCash");
        this.portfolioValue = requireNonNegative(portfolioValue, "portfolioValue");
        this.profitLoss = Objects.requireNonNull(profitLoss, "profitLoss must not be null");
        Objects.requireNonNull(holdings, "holdings must not be null");
        if (holdings.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("holdings must not contain null");
        }
        this.holdings = Collections.unmodifiableList(new ArrayList<>(holdings));
    }

    public BigDecimal getAvailableCash() {
        return availableCash;
    }

    public BigDecimal getPortfolioValue() {
        return portfolioValue;
    }

    public BigDecimal getProfitLoss() {
        return profitLoss;
    }

    public List<HoldingOutputData> getHoldings() {
        return holdings;
    }

    private static BigDecimal requireNonNegative(BigDecimal value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        if (value.signum() < 0) {
            throw new IllegalArgumentException(fieldName + " must not be negative");
        }
        return value;
    }
}
