package interface_adapter.view_budget_progress;
import use_case.view_budget_progress.ViewBudgetProgressInputBoundary;
public class ViewBudgetProgressController {
    private final ViewBudgetProgressInputBoundary interactor;
    public ViewBudgetProgressController(ViewBudgetProgressInputBoundary interactor) {
        this.interactor = interactor;
    }
    public void execute() { interactor.execute(); }
}
