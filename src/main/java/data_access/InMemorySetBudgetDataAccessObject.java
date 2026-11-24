package data_access;


import java.util.HashMap;
import java.util.Map;

import entity.Budget;
import entity.Wallet;
import use_case.set_budget.SetBudgetDataAccessInterface;

/**
 * In-memory implementation of the DAO for budgets and wallets. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemorySetBudgetDataAccessObject implements SetBudgetDataAccessInterface {

    private final Map<String, Budget> budgets = new HashMap<>();
    private final Map<String, Wallet> wallets = new HashMap<>();

    @Override
    public void saveBudget(String username, Budget budget) {
        budgets.put(username, budget);
    }

    @Override
    public void saveWallet(String username, Wallet wallet) {
        wallets.put(username, wallet);
    }

    public Budget getBudget(String username) {
        return budgets.get(username);
    }

    public Wallet getWallet(String username) {
        return wallets.get(username);
    }
}