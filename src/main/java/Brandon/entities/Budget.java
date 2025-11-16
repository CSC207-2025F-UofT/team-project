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

    public String getMonth() {
        return month;
    }

    // + updateSpent(amount)
    // + compareToBudget()
}
