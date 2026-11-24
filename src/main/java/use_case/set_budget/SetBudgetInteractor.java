package use_case.set_budget;

import java.math.BigDecimal;

import entity.Budget;
import entity.Wallet;

/**
 * The Set Budget Interactor.
 */
public class SetBudgetInteractor implements SetBudgetInputBoundary {
    private final SetBudgetDataAccessInterface dataAccess;
    private final SetBudgetOutputBoundary presenter;

    public SetBudgetInteractor(SetBudgetDataAccessInterface dataAccess,
                               SetBudgetOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SetBudgetInputData inputData) {
        final String username = inputData.getUsername();
        final String limitText = inputData.getDailyLimitText();
        final String balanceText = inputData.getBalanceText();

        if (isBlank(username) || isBlank(limitText) || isBlank(balanceText)) {
            presenter.prepareFailView("All fields must be filled in.");
            return;
        }

        final BigDecimal dailyLimit;
        try {
            dailyLimit = new BigDecimal(limitText.trim());
        }
        catch (NumberFormatException exception) {
            presenter.prepareFailView("Daily limit must be a valid number.");
            return;
        }

        final BigDecimal balance;
        try {
            balance = new BigDecimal(balanceText.trim());
        }
        catch (NumberFormatException exception) {
            presenter.prepareFailView("Balance must be a valid number.");
            return;
        }

        final Budget budget;
        try {
            budget = new Budget(dailyLimit);
        }
        catch (IllegalArgumentException exception) {
            presenter.prepareFailView(exception.getMessage());
            return;
        }
        final Wallet wallet = new Wallet(balance);

        dataAccess.saveBudget(username, budget);
        dataAccess.saveWallet(username, wallet);

        presenter.prepareSuccessView(new SetBudgetOutputData(
                username, dailyLimit.toPlainString(), balance.toPlainString()));
    }

    private static boolean isBlank(String text) {
        return text == null || text.isBlank();
    }
}