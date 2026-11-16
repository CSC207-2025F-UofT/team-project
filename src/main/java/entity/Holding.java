package entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Objects;

/**
 * Shares of one stock held in a portfolio.
 */
public class Holding {
    private static final int AVERAGE_PRICE_SCALE = 10;

    private final String ticker;
    private int quantity;
    private BigDecimal averagePurchasePrice;
    private BigDecimal currentPrice;

    public Holding(String ticker, int quantity, BigDecimal purchasePrice) {
        this(ticker, quantity, purchasePrice, purchasePrice);
    }

    public Holding(String ticker, int quantity, BigDecimal averagePurchasePrice,
                   BigDecimal currentPrice) {
        this.ticker = normalizeTicker(ticker);
        requirePositiveQuantity(quantity);
        this.averagePurchasePrice = requirePositivePrice(averagePurchasePrice);
        this.currentPrice = requireNonNegativePrice(currentPrice);
        this.quantity = quantity;
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
        return currentPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public void updateCurrentPrice(BigDecimal price) {
        currentPrice = requireNonNegativePrice(price);
    }

    public void addShares(int shares, BigDecimal purchasePrice) {
        requirePositiveQuantity(shares);
        BigDecimal price = requirePositivePrice(purchasePrice);
        BigDecimal existingCost = averagePurchasePrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal newCost = price.multiply(BigDecimal.valueOf(shares));
        int newQuantity = Math.addExact(quantity, shares);
        averagePurchasePrice = existingCost.add(newCost)
                .divide(BigDecimal.valueOf(newQuantity), AVERAGE_PRICE_SCALE,
                        RoundingMode.HALF_UP);
        quantity = newQuantity;
        currentPrice = price;
    }

    public void removeShares(int shares) {
        requirePositiveQuantity(shares);
        if (shares > quantity) {
            throw new IllegalArgumentException("Cannot sell more shares than are held");
        }
        quantity -= shares;
    }

    private static String normalizeTicker(String ticker) {
        Objects.requireNonNull(ticker, "ticker must not be null");
        String normalized = ticker.trim().toUpperCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("ticker must not be blank");
        }
        return normalized;
    }

    private static BigDecimal requireNonNegativePrice(BigDecimal price) {
        Objects.requireNonNull(price, "price must not be null");
        if (price.signum() < 0) {
            throw new IllegalArgumentException("price must not be negative");
        }
        return price;
    }

    private static BigDecimal requirePositivePrice(BigDecimal price) {
        BigDecimal validatedPrice = requireNonNegativePrice(price);
        if (validatedPrice.signum() == 0) {
            throw new IllegalArgumentException("price must be positive");
        }
        return validatedPrice;
    }

    private static void requirePositiveQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}
