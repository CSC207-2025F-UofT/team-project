package interface_adapter.transactions;

import application_business_rules.transactions.TransactionDashboardOutputBoundary;
import application_business_rules.transactions.TransactionDashboardOutputData;

public final class TransactionDashboardPresenter implements TransactionDashboardOutputBoundary {
    private final TransactionDashboardViewModel viewModel;
    public TransactionDashboardPresenter(TransactionDashboardViewModel viewModel) {
        this.viewModel = viewModel;
    }
    @Override
    public void present(TransactionDashboardOutputData output) {
        if (output.getUsers() != null && !output.getUsers().isEmpty()) viewModel.setUsers(output.getUsers());
        viewModel.setIncome(output.getIncome());
        viewModel.setExpenses(output.getExpenses());
        viewModel.setTotals(output.getTotalIncome(), output.getTotalExpenses());
        viewModel.setMessage(output.getMessage());
        viewModel.setError(false);
    }
    @Override
    public void presentError(String message) {
        viewModel.setMessage(message == null ? "An unexpected error occurred." : message);
        viewModel.setError(true);
    }
}
