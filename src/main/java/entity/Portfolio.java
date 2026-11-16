package entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * A simulated investment portfolio containing cash, holdings, and trade history.
 */
public class Portfolio {
    public static final BigDecimal DEFAULT_INITIAL_BALANCE = new BigDecimal("10000.00");

    private final BigDecimal initialBalance;
    private BigDecimal availableCash;
    private final Map<String, Holding> holdings = new LinkedHashMap<>();
    private final List<Transaction> transactions = new ArrayList<>();

    public Portfolio() {
        this(DEFAULT_INITIAL_BALANCE);
    }

    public Portfolio(BigDecimal initialBalance) {
        this.initialBalance = requireNonNegativeAmount(initialBalance);
        this.availableCash = this.initialBalance;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public BigDecimal getAvailableCash() {
        return availableCash;
    }

    public Map<String, Holding> getHoldings() {
        Map<String, Holding> copies = new LinkedHashMap<>();
        for (Map.Entry<String, Holding> entry : holdings.entrySet()) {
            Holding original = entry.getValue();
            copies.put(entry.getKey(), new Holding(
                    original.getTicker(),
                    original.getQuantity(),
                    original.getAveragePurchasePrice(),
                    original.getCurrentPrice()));
        }
        return Collections.unmodifiableMap(copies);
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void buy(String ticker, int quantity, BigDecimal price) {
        requirePositiveQuantity(quantity);
        BigDecimal tradePrice = requirePositiveAmount(price);
        BigDecimal cost = tradePrice.multiply(BigDecimal.valueOf(quantity));
        if (cost.compareTo(availableCash) > 0) {
            throw new IllegalArgumentException("Insufficient available cash");
        }

        String normalizedTicker = normalizeTicker(ticker);
        Holding holding = holdings.get(normalizedTicker);
        if (holding == null) {
            holdings.put(normalizedTicker, new Holding(normalizedTicker, quantity, tradePrice));
        } else {
            holding.addShares(quantity, tradePrice);
        }
        availableCash = availableCash.subtract(cost);
        transactions.add(new Transaction(normalizedTicker, quantity, tradePrice,
                Transaction.Type.BUY));
    }

    public void sell(String ticker, int quantity, BigDecimal price) {
        requirePositiveQuantity(quantity);
        BigDecimal tradePrice = requirePositiveAmount(price);
        String normalizedTicker = normalizeTicker(ticker);
        Holding holding = holdings.get(normalizedTicker);
        if (holding == null || quantity > holding.getQuantity()) {
            throw new IllegalArgumentException("Cannot sell more shares than are held");
        }

        holding.removeShares(quantity);
        availableCash = availableCash.add(tradePrice.multiply(BigDecimal.valueOf(quantity)));
        transactions.add(new Transaction(normalizedTicker, quantity, tradePrice,
                Transaction.Type.SELL));
        if (holding.getQuantity() == 0) {
            holdings.remove(normalizedTicker);
        }
    }

    public void updateCurrentPrice(String ticker, BigDecimal price) {
        Holding holding = holdings.get(normalizeTicker(ticker));
        if (holding == null) {
            throw new IllegalArgumentException("No holding exists for ticker");
        }
        holding.updateCurrentPrice(requireNonNegativeAmount(price));
    }

    public BigDecimal getPortfolioValue() {
        BigDecimal holdingsValue = BigDecimal.ZERO;
        for (Holding holding : holdings.values()) {
            holdingsValue = holdingsValue.add(holding.getMarketValue());
        }
        return availableCash.add(holdingsValue);
    }

    public BigDecimal getProfitLoss() {
        return getPortfolioValue().subtract(initialBalance);
    }

    private static String normalizeTicker(String ticker) {
        Objects.requireNonNull(ticker, "ticker must not be null");
        String normalized = ticker.trim().toUpperCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("ticker must not be blank");
        }
        return normalized;
    }

    private static BigDecimal requireNonNegativeAmount(BigDecimal amount) {
        Objects.requireNonNull(amount, "amount must not be null");
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("amount must not be negative");
        }
        return amount;
    }

    private static BigDecimal requirePositiveAmount(BigDecimal amount) {
        BigDecimal validatedAmount = requireNonNegativeAmount(amount);
        if (validatedAmount.signum() == 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        return validatedAmount;
    }

    private static void requirePositiveQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}
