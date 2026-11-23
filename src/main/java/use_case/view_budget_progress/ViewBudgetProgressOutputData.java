package use_case.view_budget_progress;
public class ViewBudgetProgressOutputData {
    private final java.time.LocalDate date;
    private final java.math.BigDecimal limit;
    private final java.math.BigDecimal spent;
    private final java.math.BigDecimal remaining;
    private final java.math.BigDecimal percentage;
    public ViewBudgetProgressOutputData(java.time.LocalDate date, java.math.BigDecimal limit, java.math.BigDecimal spent, java.math.BigDecimal remaining, java.math.BigDecimal percentage) {
        this.date = date;
        this.limit = limit;
        this.spent = spent;
        this.remaining = remaining;
        this.percentage = percentage;
    }
    public java.time.LocalDate getDate() { return date; }
    public java.math.BigDecimal getLimit() { return limit; }
    public java.math.BigDecimal getSpent() { return spent; }
    public java.math.BigDecimal getRemaining() { return remaining; }
    public java.math.BigDecimal getPercentage() { return percentage; }
}
