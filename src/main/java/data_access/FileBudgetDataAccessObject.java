package data_access;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import entity.Budget;
import entity.Wallet;
import org.json.JSONObject;
import use_case.set_budget.SetBudgetDataAccessInterface;
import use_case.view_budget_progress.ViewBudgetProgressBudgetDataAccessInterface;

/**
 * DAO for budgets and wallets, stored in a local JSON file.
 * The file looks like: {"Paul": {"dailyLimit": "50", "balance": "200"}}
 */
public class FileBudgetDataAccessObject implements SetBudgetDataAccessInterface, ViewBudgetProgressBudgetDataAccessInterface {

    private final Path file;
    private final Map<String, Budget> budgets = new HashMap<>();
    private final Map<String, Wallet> wallets = new HashMap<>();

    public FileBudgetDataAccessObject(String jsonPath) {
        this.file = Path.of(jsonPath).toAbsolutePath();
        try {
            if (Files.exists(file) && Files.size(file) > 0) {
                load();
            }
        }
        catch (IOException exception) {
            throw new UncheckedIOException("Could not read budgets file.", exception);
        }
    }

    private void load() throws IOException {
        final JSONObject root = new JSONObject(Files.readString(file, StandardCharsets.UTF_8));
        for (String username : root.keySet()) {
            final JSONObject entry = root.getJSONObject(username);
            if (entry.has("dailyLimit")) {
                budgets.put(username, new Budget(new BigDecimal(entry.getString("dailyLimit"))));
            }
            if (entry.has("balance")) {
                wallets.put(username, new Wallet(new BigDecimal(entry.getString("balance"))));
            }
        }
    }

    private void write() throws IOException {
        final JSONObject root = new JSONObject();
        for (Map.Entry<String, Budget> budget : budgets.entrySet()) {
            entryFor(root, budget.getKey())
                    .put("dailyLimit", budget.getValue().getLimit().toPlainString());
        }
        for (Map.Entry<String, Wallet> wallet : wallets.entrySet()) {
            entryFor(root, wallet.getKey())
                    .put("balance", wallet.getValue().getBalance().toPlainString());
        }
        Files.writeString(file, root.toString(2), StandardCharsets.UTF_8);
    }

    private static JSONObject entryFor(JSONObject root, String username) {
        if (!root.has(username)) {
            root.put(username, new JSONObject());
        }
        return root.getJSONObject(username);
    }

    @Override
    public void saveBudget(String username, Budget budget) {
        budgets.put(username, budget);
        save();
    }

    @Override
    public void saveWallet(String username, Wallet wallet) {
        wallets.put(username, wallet);
        save();
    }

    @Override
    public BigDecimal getDailyLimit(String username) {
        final Budget budget = budgets.get(username);
        return budget == null ? null : budget.getLimit();
    }

    private void save() {
        try {
            write();
        }
        catch (IOException exception) {
            throw new UncheckedIOException("Could not save budgets file.", exception);
        }
    }

    public Budget getBudget(String username) {
        return budgets.get(username);
    }

    public Wallet getWallet(String username) {
        return wallets.get(username);
    }
}