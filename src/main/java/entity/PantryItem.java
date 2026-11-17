package entity;

import java.time.LocalDate;
import java.util.Objects;

/**
 * An ingredient stored in the user's pantry.
 */
public class PantryItem {
    private final IngredientName name;
    private final LocalDate expiryDate;
    private StockLevel stockLevel;

    /**
     * Creates a pantry item.
     *
     * @param name the ingredient name
     * @param expiryDate the expiry date, or {@code null} when none is known
     * @param stockLevel the current stock level
     */
    public PantryItem(IngredientName name, LocalDate expiryDate, StockLevel stockLevel) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.expiryDate = expiryDate;
        this.stockLevel = Objects.requireNonNull(stockLevel, "stock level cannot be null");
    }

    public IngredientName getName() {
        return name;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public StockLevel getStockLevel() {
        return stockLevel;
    }

    void setStockLevel(StockLevel stockLevel) {
        this.stockLevel = Objects.requireNonNull(stockLevel, "stock level cannot be null");
    }

    /**
     * Returns whether this item expired before the supplied date.
     *
     * @param today the date used for the comparison
     * @return true when the item has an expiry date before today
     */
    public boolean isExpired(LocalDate today) {
        Objects.requireNonNull(today, "today cannot be null");
        return expiryDate != null && expiryDate.isBefore(today);
    }

    /**
     * Returns whether this item expires between today and the end of the supplied window,
     * inclusive. Already-expired items are excluded.
     *
     * @param today the first date in the window
     * @param days the number of days after today included in the window
     * @return true when the expiry date is in the window
     */
    public boolean expiresWithin(LocalDate today, int days) {
        Objects.requireNonNull(today, "today cannot be null");
        if (days < 0) {
            throw new IllegalArgumentException("days cannot be negative");
        }

        return expiryDate != null
                && !expiryDate.isBefore(today)
                && !expiryDate.isAfter(today.plusDays(days));
    }
}
