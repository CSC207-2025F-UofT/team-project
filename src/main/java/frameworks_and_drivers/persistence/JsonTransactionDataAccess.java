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
import java.util.List;

/** Account-scoped transaction gateway using the same Profiles.json as account persistence. */
public final class JsonTransactionDataAccess implements TransactionDataAccessInterface {
    private final Path profilesPath;
    private final Path legacyTransactionsPath;

    public JsonTransactionDataAccess(Path profilesPath, Path legacyTransactionsPath) {
        this.profilesPath = profilesPath;
        this.legacyTransactionsPath = legacyTransactionsPath;
    }

    private JSONObject readProfiles() throws IOException {
        if (!Files.exists(profilesPath)) throw new IOException("Missing profile data: " + profilesPath);
        JSONObject profiles = new JSONObject(Files.readString(profilesPath));
        if (profiles.getJSONArray("profiles").length() == 0 && Files.exists(legacyTransactionsPath)) {
            JSONObject legacy = new JSONObject(Files.readString(legacyTransactionsPath));
            if (legacy.has("users")) {
                profiles = migrateLegacyUsers(profiles, legacy.getJSONObject("users"));
                write(profilesPath, profiles);
            }
        }
        return profiles;
    }

    private void write(Path path, JSONObject data) throws IOException {
        if (path.getParent() != null) Files.createDirectories(path.getParent());
        Files.writeString(path, data.toString(2));
    }

    /** Preserve old user-keyed demo transactions by moving them into each user's Main Chequing account. */
    private JSONObject migrateLegacyUsers(JSONObject base, JSONObject users) {
        JSONArray profiles = new JSONArray();
        int profileId = 1;
        int accountId = 1;
        for (String userName : users.keySet()) {
            JSONObject oldUser = users.getJSONObject(userName);
            JSONArray oldIncome = oldUser.optJSONArray("income");
            JSONArray oldExpenses = oldUser.optJSONArray("expenses");
            if (oldIncome == null) oldIncome = new JSONArray();
            if (oldExpenses == null) oldExpenses = new JSONArray();
            BigDecimal balance = sum(oldIncome).subtract(sum(oldExpenses));
            if (balance.signum() < 0) balance = BigDecimal.ZERO;
            String createdAt = LocalDateTime.now().toString();

            JSONObject chequing = new JSONObject()
                .put("id", accountId++)
                .put("name", "Main Chequing")
                .put("type", AccountType.CHEQUING.name())
                .put("balance", balance.toPlainString())
                .put("currency", "CAD")
                .put("overdraftLimit", "0")
                .put("createdAt", createdAt)
                .put("income", oldIncome)
                .put("expenses", oldExpenses);

            JSONObject savings = new JSONObject()
                .put("id", accountId++)
                .put("name", "Savings")
                .put("type", AccountType.SAVINGS.name())
                .put("balance", "0.00")
                .put("currency", "CAD")
                .put("annualInterestRate", "0.02")
                .put("createdAt", createdAt)
                .put("income", new JSONArray())
                .put("expenses", new JSONArray());

            JSONArray accounts = new JSONArray().put(chequing).put(savings);
            profiles.put(new JSONObject()
                .put("id", profileId++)
                .put("name", userName)
                .put("createdAt", createdAt)
                .put("accounts", accounts));
        }
        base.put("nextUserId", profileId);
        base.put("nextAccountId", accountId);
        base.put("profiles", profiles);
        return base;
    }

    private BigDecimal sum(JSONArray rows) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < rows.length(); i++) {
            total = total.add(new BigDecimal(rows.getJSONObject(i).get("amount").toString()));
        }
        return total;
    }

    @Override
    public List<Account> getAccounts() throws IOException {
        JSONArray profiles = readProfiles().getJSONArray("profiles");
        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < profiles.length(); i++) {
            JSONObject profile = profiles.getJSONObject(i);
            JSONArray rows = profile.getJSONArray("accounts");
            for (int j = 0; j < rows.length(); j++) {
                accounts.add(deserializeAccount(rows.getJSONObject(j), profile.getInt("id")));
            }
        }
        return accounts;
    }

    @Override
    public List<Transaction> getTransactions(int accountId, TransactionType type) throws IOException {
        JSONObject account = findAccount(readProfiles(), accountId);
        String key = type == TransactionType.INCOME ? "income" : "expenses";
        JSONArray rows = account.optJSONArray(key);
        List<Transaction> result = new ArrayList<>();
        if (rows == null) return result;
        for (int i = 0; i < rows.length(); i++) {
            JSONObject row = rows.getJSONObject(i);
            result.add(new Transaction(row.getInt("id"), type,
                new BigDecimal(row.get("amount").toString()), row.getString("description"),
                LocalDate.parse(row.getString("date"))));
        }
        return result;
    }

    @Override
    public void addTransaction(int accountId, Transaction transaction) throws IOException {
        JSONObject data = readProfiles();
        JSONObject row = findAccount(data, accountId);
        Account account = deserializeAccount(row, findOwnerId(data, accountId));
        if (transaction.getType() == TransactionType.INCOME) account.deposit(transaction.getAmount());
        else account.withdraw(transaction.getAmount());

        String key = transaction.getType() == TransactionType.INCOME ? "income" : "expenses";
        JSONArray rows = row.optJSONArray(key);
        if (rows == null) { rows = new JSONArray(); row.put(key, rows); }
        int nextId = 1;
        for (int i = 0; i < rows.length(); i++) nextId = Math.max(nextId, rows.getJSONObject(i).getInt("id") + 1);
        rows.put(new JSONObject()
            .put("id", nextId)
            .put("amount", transaction.getAmount())
            .put("description", transaction.getDescription())
            .put("date", transaction.getDate().toString()));
        row.put("balance", account.getBalance().toPlainString());
        write(profilesPath, data);
    }

    @Override
    public boolean deleteTransaction(int accountId, TransactionType type, int transactionId) throws IOException {
        JSONObject data = readProfiles();
        JSONObject row = findAccount(data, accountId);
        String key = type == TransactionType.INCOME ? "income" : "expenses";
        JSONArray rows = row.optJSONArray(key);
        if (rows == null) return false;
        for (int i = 0; i < rows.length(); i++) {
            JSONObject transaction = rows.getJSONObject(i);
            if (transaction.getInt("id") == transactionId) {
                BigDecimal amount = new BigDecimal(transaction.get("amount").toString());
                Account account = deserializeAccount(row, findOwnerId(data, accountId));
                if (type == TransactionType.INCOME) account.withdraw(amount);
                else account.deposit(amount);
                rows.remove(i);
                row.put("balance", account.getBalance().toPlainString());
                write(profilesPath, data);
                return true;
            }
        }
        return false;
    }

    private JSONObject findAccount(JSONObject data, int accountId) {
        JSONArray profiles = data.getJSONArray("profiles");
        for (int i = 0; i < profiles.length(); i++) {
            JSONArray accounts = profiles.getJSONObject(i).getJSONArray("accounts");
            for (int j = 0; j < accounts.length(); j++) {
                JSONObject account = accounts.getJSONObject(j);
                if (account.getInt("id") == accountId) return account;
            }
        }
        throw new IllegalArgumentException("Account could not be found: " + accountId);
    }

    private int findOwnerId(JSONObject data, int accountId) {
        JSONArray profiles = data.getJSONArray("profiles");
        for (int i = 0; i < profiles.length(); i++) {
            JSONObject profile = profiles.getJSONObject(i);
            JSONArray accounts = profile.getJSONArray("accounts");
            for (int j = 0; j < accounts.length(); j++) {
                if (accounts.getJSONObject(j).getInt("id") == accountId) return profile.getInt("id");
            }
        }
        throw new IllegalArgumentException("Account could not be found: " + accountId);
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
            default: throw new IllegalArgumentException("Unsupported account type.");
        }
    }
}
