package Brandon.useCase;

public class SetBudgetInputData {
    private final String month;
    private final float limit;

    public SetBudgetInputData(String month, float limit) {
        this.month = month;
        this.limit = limit;
    }

    public String getMonth() {return month;}
    public float getLimit() {return limit;}
}
