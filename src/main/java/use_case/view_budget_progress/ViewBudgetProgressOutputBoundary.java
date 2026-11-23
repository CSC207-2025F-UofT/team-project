package use_case.view_budget_progress;
public interface ViewBudgetProgressOutputBoundary {
    void prepareSuccessView(ViewBudgetProgressOutputData data);
    void prepareFailView(String error);
}
