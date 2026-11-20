package use_case.portfolio;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

/**
 * Presentation data for one portfolio holding.
 */
public final class HoldingOutputData {
    private final String ticker;
    private final int quantity;
    private final BigDecimal averagePurchasePrice;
    private final BigDecimal currentPrice;
    private final BigDecimal marketValue;

    public HoldingOutputData(String ticker, int quantity, BigDecimal averagePurchasePrice,
                             BigDecimal currentPrice, BigDecimal marketValue) {
        this.ticker = requireNonBlank(ticker, "ticker").toUpperCase(Locale.ROOT);
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        this.quantity = quantity;
        this.averagePurchasePrice = requireNonNegative(
                averagePurchasePrice, "averagePurchasePrice");
        this.currentPrice = requireNonNegative(currentPrice, "currentPrice");
        this.marketValue = requireNonNegative(marketValue, "marketValue");
    }

    public String getTicker() {
        return ticker;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getAveragePurchasePrice() {
        return averagePurchasePrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    private static String requireNonBlank(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return trimmed;
    }

    private static BigDecimal requireNonNegative(BigDecimal value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        if (value.signum() < 0) {
            throw new IllegalArgumentException(fieldName + " must not be negative");
        }
        return value;
    }
}
