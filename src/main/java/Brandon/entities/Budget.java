package Brandon.entities;

public class Budget {
    private int budgetID;
    private String month;
    private float limit;
    private float totalSpent;

    public Budget (String month) {
        this.month = month;
        this.totalSpent = 0;
    }

    public void setLimit (float limit) {
        this.limit = limit;
    }

    public void setTotalSpent (float totalSpent) {
        this.totalSpent = totalSpent;
    }

    public String getMonth() {
        return month;
    }

    public float getTotalSpent() {
        return totalSpent;
    }

    public float getRemaining() {
        return limit - totalSpent;
    }

    // + updateSpent(amount)
    // + compareToBudget()
}
