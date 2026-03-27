package entity;

import java.util.Map;

/**
 * Spending Report class.
 */
public class SpendingReport {

    private final float totalSpent;
    private final Map<String, Float> categoryBreakdown;

    public SpendingReport(Map<String, Float> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
        this.totalSpent = categoryBreakdown.values().stream().reduce(0f, Float::sum);
    }

    public float getTotalSpent() {
        return totalSpent;
    }

    public Map<String, Float> getCategoryBreakdown() {
        return categoryBreakdown;
    }
}
