package use_case.set_budget;

import entity.Budget;
import entity.Wallet;

/**
 * The DAO interface for the Set Budget Use Case.
 */
public interface SetBudgetDataAccessInterface {

    /**
     * Saves the budget for the given user, replacing any existing one.
     * @param username the user who owns the budget
     * @param budget the budget to save
     */
    void saveBudget(String username, Budget budget);

    /**
     * Saves the wallet for the given user, replacing any existing one.
     * @param username the user who owns the wallet
     * @param wallet the wallet to save
     */
    void saveWallet(String username, Wallet wallet);
}