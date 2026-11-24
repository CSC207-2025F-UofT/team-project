package frameworks_and_drivers.persistence;

import use_case.transactions.TransactionDataAccessInterface;
import entity.Account;
import entity.AccountType;
import entity.CashAccount;
import entity.ChequingAccount;
import entity.SavingsAccount;
import entity.Transaction;
import entity.TransactionType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Transaction gateway backed by the shared Profiles.json database.
 * The transaction use case remains username-based; this adapter stores each
 * transaction under the user's first account (their default account).
 */
public final class JsonTransactionDataAccess implements TransactionDataAccessInterface {
    private final Path filePath;

    public JsonTransactionDataAccess(Path filePath) { this.filePath = filePath; }

    private JSONObject read() throws IOException {
        if (!Files.exists(filePath)) throw new IOException("Missing profile database: " + filePath);
        return new JSONObject(Files.readString(filePath));
    }

    private void write(JSONObject data) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) Files.createDirectories(parent);
        Files.writeString(filePath, data.toString(2));
    }

    @Override
    public List<String> getUserNames() throws IOException {
        JSONArray profiles = read().getJSONArray("profiles");
        List<String> names = new ArrayList<>();
        for (int i = 0; i < profiles.length(); i++) names.add(profiles.getJSONObject(i).getString("name"));
        names.sort(String.CASE_INSENSITIVE_ORDER);
        return names;
    }

    @Override
    public List<Transaction> getTransactions(String userName, TransactionType type) throws IOException {
        JSONObject profile = findProfile(read(), userName);
        JSONArray accounts = profile.optJSONArray("accounts");
        List<Transaction> result = new ArrayList<>();
        if (accounts == null) return result;

        String key = transactionKey(type);
        for (int i = 0; i < accounts.length(); i++) {
            JSONArray rows = accounts.getJSONObject(i).optJSONArray(key);
            if (rows == null) continue;
            for (int j = 0; j < rows.length(); j++) {
                JSONObject row = rows.getJSONObject(j);
                result.add(new Transaction(row.getInt("id"), type,
                    new BigDecimal(row.get("amount").toString()), row.getString("description"),
                    LocalDate.parse(row.getString("date"))));
            }
        }
        result.sort(Comparator.comparingInt(Transaction::getId));
        return result;
    }

    @Override
    public void addTransaction(String userName, Transaction transaction) throws IOException {
        JSONObject data = read();
        JSONObject profile = findProfile(data, userName);
        JSONArray accounts = profile.optJSONArray("accounts");
        if (accounts == null || accounts.length() == 0) {
            throw new IllegalArgumentException("Create an account for this profile before adding transactions.");
        }

        // Keep the current transaction use case intact: its selected user maps
        // to that user's first (default) account until account selection is added.
        JSONObject accountRow = accounts.getJSONObject(0);
        Account account = deserializeAccount(accountRow, profile.getInt("id"));
        if (transaction.getType() == TransactionType.INCOME) account.deposit(transaction.getAmount());
        else account.withdraw(transaction.getAmount());

        String key = transactionKey(transaction.getType());
        JSONArray rows = accountRow.optJSONArray(key);
        if (rows == null) { rows = new JSONArray(); accountRow.put(key, rows); }

        int nextId = 1;
        for (int i = 0; i < accounts.length(); i++) {
            JSONObject candidate = accounts.getJSONObject(i);
            for (String transactionKey : new String[]{"income", "expenses"}) {
                JSONArray existing = candidate.optJSONArray(transactionKey);
                if (existing == null) continue;
                for (int j = 0; j < existing.length(); j++)
                    nextId = Math.max(nextId, existing.getJSONObject(j).getInt("id") + 1);
            }
        }

        rows.put(new JSONObject()
            .put("id", nextId)
            .put("amount", transaction.getAmount())
            .put("description", transaction.getDescription())
            .put("date", transaction.getDate().toString()));
        accountRow.put("balance", account.getBalance().toPlainString());
        write(data);
    }

    @Override
    public boolean deleteTransaction(String userName, TransactionType type, int id) throws IOException {
        JSONObject data = read();
        JSONObject profile = findProfile(data, userName);
        JSONArray accounts = profile.optJSONArray("accounts");
        if (accounts == null) return false;
        String key = transactionKey(type);

        for (int i = 0; i < accounts.length(); i++) {
            JSONObject accountRow = accounts.getJSONObject(i);
            JSONArray rows = accountRow.optJSONArray(key);
            if (rows == null) continue;
            for (int j = 0; j < rows.length(); j++) {
                JSONObject row = rows.getJSONObject(j);
                if (row.getInt("id") == id) {
                    BigDecimal amount = new BigDecimal(row.get("amount").toString());
                    Account account = deserializeAccount(accountRow, profile.getInt("id"));
                    if (type == TransactionType.INCOME) account.withdraw(amount);
                    else account.deposit(amount);
                    rows.remove(j);
                    accountRow.put("balance", account.getBalance().toPlainString());
                    write(data);
                    return true;
                }
            }
        }
        return false;
    }

    private JSONObject findProfile(JSONObject data, String userName) {
        JSONArray profiles = data.getJSONArray("profiles");
        for (int i = 0; i < profiles.length(); i++) {
            JSONObject profile = profiles.getJSONObject(i);
            if (profile.getString("name").equalsIgnoreCase(userName)) return profile;
        }
        throw new IllegalArgumentException("User not found: " + userName);
    }

    private String transactionKey(TransactionType type) {
        return type == TransactionType.INCOME ? "income" : "expenses";
    }

    private Account deserializeAccount(JSONObject row, int ownerId) {
        int id = row.getInt("id");
        String name = row.getString("name");
        BigDecimal balance = new BigDecimal(row.getString("balance"));
        String currency = row.getString("currency");
        LocalDateTime createdAt = LocalDateTime.parse(row.getString("createdAt"));
        switch (AccountType.valueOf(row.getString("type"))) {
            case SAVINGS:
                return new SavingsAccount(id, ownerId, name, balance, currency,
                    new BigDecimal(row.optString("annualInterestRate", "0")), createdAt);
            case CHEQUING:
                return new ChequingAccount(id, ownerId, name, balance, currency,
                    new BigDecimal(row.optString("overdraftLimit", "0")), createdAt);
            case CASH:
                return new CashAccount(id, ownerId, name, balance, currency,
                    row.optString("storageLocation", "Wallet"), createdAt);
            default:
                throw new IllegalArgumentException("Unsupported account type.");
        }
    }
}
