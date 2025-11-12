package data_access;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Transaction;
import entity.User;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

interface TransactionDataAccess {
    List<Transaction> getTransactions(int userId, String month);
}

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject implements SignupUserDataAccessInterface,
                                                     LoginUserDataAccessInterface,
                                                     ChangePasswordUserDataAccessInterface,
                                                     LogoutUserDataAccessInterface,
                                                     TransactionDataAccess {

    private final Map<String, User> users = new HashMap<>();
    private final Map<Integer, List<Transaction>> transactions = new HashMap<>();

    public InMemoryUserDataAccessObject() {
        // fake data for testing
        transactions.put(1, Arrays.asList(
            new Transaction(1, 1, "2025-09-02", "Walmart", 120.50f, "Groceries"),
            new Transaction(2, 1, "2025-09-10", "Starbucks", 15.00f, "Dining"),
            new Transaction(3, 1, "2025-09-20", "Amazon", 85.00f, "Shopping")
        ));
    }

    private String currentUsername;

    @Override
    public boolean existsByName(String identifier) {
        return users.containsKey(identifier);
    }

    @Override
    public void save(User user) {
        users.put(user.getName(), user);
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public void changePassword(User user) {
        // Replace the old entry with the new password
        users.put(user.getName(), user);
    }

    @Override
    public List<Transaction> getTransactions(int userId, String month) {
        List<Transaction> all = new ArrayList<>();
        for (List<Transaction> list : transactions.values()) {
            all.addAll(list);
        }
        return all;
    }
}
