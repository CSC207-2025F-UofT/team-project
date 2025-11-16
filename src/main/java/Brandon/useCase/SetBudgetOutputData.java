package Brandon.useCase;

public class SetBudgetOutputData {
    private final String month;
    private final float limit;
    private final boolean success;
    private final String message;

    public SetBudgetOutputData(String month, float limit, boolean success, String message) {
        this.month = month;
        this.limit = limit;
        this.success = success;
        this.message = message;
    }

    public String getMonth() {return month;}
    public float getLimit() {return limit;}
    public boolean getSuccess() {return success;}
    public String getMessage() {return message;}
}
