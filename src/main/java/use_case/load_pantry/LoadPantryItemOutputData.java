package use_case.load_pantry;

import java.time.LocalDate;

import entity.StockLevel;

/**
 * Display-ready data for one pantry item.
 */
public final class LoadPantryItemOutputData {
    private final String displayName;
    private final String ingredientKey;
    private final LocalDate expiryDate;
    private final StockLevel stockLevel;
    private final boolean expired;
    private final boolean expiringSoon;

    /**
     * Creates output data for one pantry item.
     *
     * @param displayName the ingredient name shown to the user
     * @param ingredientKey the normalized ingredient identifier
     * @param expiryDate the expiry date, or {@code null} when none is set
     * @param stockLevel the current stock level
     * @param expired whether the item has already expired
     * @param expiringSoon whether the item expires within three days
     */
    public LoadPantryItemOutputData(String displayName, String ingredientKey,
                                    LocalDate expiryDate, StockLevel stockLevel,
                                    boolean expired, boolean expiringSoon) {
        this.displayName = displayName;
        this.ingredientKey = ingredientKey;
        this.expiryDate = expiryDate;
        this.stockLevel = stockLevel;
        this.expired = expired;
        this.expiringSoon = expiringSoon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIngredientKey() {
        return ingredientKey;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public StockLevel getStockLevel() {
        return stockLevel;
    }

    public boolean isExpired() {
        return expired;
    }

    public boolean isExpiringSoon() {
        return expiringSoon;
    }
}
