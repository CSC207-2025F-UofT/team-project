public class SpendingReport {
    private String month;
    private float totalSpent;
    private Map<String, Float> categoryBreakdown;

    public SpendingReport(String month, Map<String, Float> categoryBreakdown) {
        this.month = month;
        this.categoryBreakdown = categoryBreakdown;
        this.totalSpent = categoryBreakdown.values().stream().reduce(0f, Float::sum);
    }

    public String getMonth() { return month; }
    public float getTotalSpent() { return totalSpent; }
    public Map<String, Float> getCategoryBreakdown() { return categoryBreakdown; }
}
