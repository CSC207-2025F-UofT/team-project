package interface_adapter.view_budget_progress;
public class ViewBudgetProgressState {
    private final String message;
    private final int percentage;
    private final boolean error;
    public ViewBudgetProgressState(String message, int percentage, boolean error) {
        this.message = message;
        this.percentage = percentage;
        this.error = error;
    }
    public String getMessage() { return message; }
    public int getPercentage() { return percentage; }
    public boolean isError() { return error; }
}
