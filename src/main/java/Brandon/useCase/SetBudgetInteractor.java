package Brandon.useCase;

import Brandon.entities.Budget;

public class SetBudgetInteractor implements SetBudgetInputBoundary {

    private final SetBudgetDataAccessInterface budgetDataAccess;
    private final SetBudgetOutputBoundary presenter;

    public SetBudgetInteractor(SetBudgetDataAccessInterface budgetDataAccess,
                               SetBudgetOutputBoundary presenter) {
        this.budgetDataAccess = budgetDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SetBudgetInputData inputData) {
        String month = inputData.getMonth();
        float limit = inputData.getLimit();

        // Validate
        if (limit < 0) {
            SetBudgetOutputData outputData =
                    new SetBudgetOutputData(month, limit, false,
                            "Budget cannot be negative.");
            presenter.present(outputData);
            return;
        }

        if (month == null || month.isBlank()) {
            SetBudgetOutputData outputData =
                    new SetBudgetOutputData(month, limit, false,
                            "Month cannot be empty.");
            presenter.present(outputData);
            return;
        }

        // Load or create Budget entity
        Budget budget = budgetDataAccess.getBudgetForMonth(month);
        if (budget == null) {
            budget = new Budget(month);
        }
        budget.setLimit(limit);

        // Save through DAO
        budgetDataAccess.saveBudget(budget);

        // Build output and notify presenter
        SetBudgetOutputData outputData =
                new SetBudgetOutputData(month, limit, true,
                        "Budget set successfully.");
        presenter.present(outputData);
    }
}
