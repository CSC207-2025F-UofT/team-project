package interface_adapter.set_budget;

import use_case.set_budget.SetBudgetInputBoundary;
import use_case.set_budget.SetBudgetInputData;

/**
 * Controller for the Set Budget use case.
 */
public class SetBudgetController {
    private final SetBudgetInputBoundary setBudgetUseCaseInteractor;

    public SetBudgetController(SetBudgetInputBoundary setBudgetUseCaseInteractor) {
        this.setBudgetUseCaseInteractor = setBudgetUseCaseInteractor;
    }

    /**
     * Executes the Set Budget use case.
     * @param username the user setting their budget
     * @param dailyLimit the user's daily budget limit
     * @param balance the user's wallet balance
     */
    public void execute(String username, String dailyLimit, String balance) {
        final SetBudgetInputData setBudgetInputData = new SetBudgetInputData(username, dailyLimit, balance);

        setBudgetUseCaseInteractor.execute(setBudgetInputData);
    }
}