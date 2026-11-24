package interface_adapter.view_budget_progress;
import use_case.view_budget_progress.ViewBudgetProgressOutputBoundary;
import use_case.view_budget_progress.ViewBudgetProgressOutputData;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ViewBudgetProgressPresenter implements ViewBudgetProgressOutputBoundary {
    private final ViewBudgetProgressViewModel viewModel;
    public ViewBudgetProgressPresenter(ViewBudgetProgressViewModel viewModel) { this.viewModel = viewModel; }
    private String money(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
    @Override
    public void prepareSuccessView(ViewBudgetProgressOutputData data) {
        boolean over = data.getRemaining().signum() < 0;
        String message = "Date (UTC): " + data.getDate()
                + "\nDaily budget: " + money(data.getLimit())
                + "\nSpent today: " + money(data.getSpent())
                + (over ? "\nOver budget: " : "\nRemaining: ")
                + money(data.getRemaining().abs())
                + "\nBudget used: " + data.getPercentage().toPlainString() + "%"
                + "\nBudget days start at 00:00 UTC.";
        int bar = data.getPercentage().min(new BigDecimal("100")).max(BigDecimal.ZERO).intValue();
        viewModel.setState(new ViewBudgetProgressState(message, bar, false));
        viewModel.notifyStateChanged();
    }
    @Override
    public void prepareFailView(String error) {
        viewModel.setState(new ViewBudgetProgressState(error, 0, true));
        viewModel.notifyStateChanged();
    }
}
