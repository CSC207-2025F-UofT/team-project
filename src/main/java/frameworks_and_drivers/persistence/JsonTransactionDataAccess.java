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
 * The existing transaction use-case contract remains unchanged: its String
 * selector now identifies an account as "Profile name — Account name".
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
        List<String> accountChoices = new ArrayList<>();
        for (int i = 0; i < profiles.length(); i++) {
            JSONObject profile = profiles.getJSONObject(i);
            JSONArray accounts = profile.optJSONArray("accounts");
            if (accounts == null) continue;
            for (int j = 0; j < accounts.length(); j++) {
                JSONObject account = accounts.getJSONObject(j);
                accountChoices.add(selectionLabel(profile, account));
            }
        }
        accountChoices.sort(String.CASE_INSENSITIVE_ORDER);
        return accountChoices;
    }

    @Override
    public List<Transaction> getTransactions(String accountSelection, TransactionType type) throws IOException {
        AccountSelection selected = findSelectedAccount(read(), accountSelection);
        JSONArray rows = selected.account.optJSONArray(transactionKey(type));
        List<Transaction> result = new ArrayList<>();
        if (rows == null) return result;
        for (int i = 0; i < rows.length(); i++) {
            JSONObject row = rows.getJSONObject(i);
            result.add(new Transaction(row.getInt("id"), type,
                new BigDecimal(row.get("amount").toString()), row.getString("description"),
                LocalDate.parse(row.getString("date"))));
        }
        result.sort(Comparator.comparingInt(Transaction::getId));
        return result;
    }

    @Override
    public void addTransaction(String accountSelection, Transaction transaction) throws IOException {
        JSONObject data = read();
        AccountSelection selected = findSelectedAccount(data, accountSelection);
        JSONObject accountRow = selected.account;
        Account account = deserializeAccount(accountRow, selected.profile.getInt("id"));
        if (transaction.getType() == TransactionType.INCOME) account.deposit(transaction.getAmount());
        else account.withdraw(transaction.getAmount());

        String key = transactionKey(transaction.getType());
        JSONArray rows = accountRow.optJSONArray(key);
        if (rows == null) { rows = new JSONArray(); accountRow.put(key, rows); }

        int nextId = 1;
        JSONArray profiles = data.getJSONArray("profiles");
        for (int i = 0; i < profiles.length(); i++) {
            JSONArray accounts = profiles.getJSONObject(i).optJSONArray("accounts");
            if (accounts == null) continue;
            for (int j = 0; j < accounts.length(); j++) {
                JSONObject candidate = accounts.getJSONObject(j);
                for (String transactionKey : new String[]{"income", "expenses"}) {
                    JSONArray existing = candidate.optJSONArray(transactionKey);
                    if (existing == null) continue;
                    for (int k = 0; k < existing.length(); k++)
                        nextId = Math.max(nextId, existing.getJSONObject(k).getInt("id") + 1);
                }
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
    public boolean deleteTransaction(String accountSelection, TransactionType type, int id) throws IOException {
        JSONObject data = read();
        AccountSelection selected = findSelectedAccount(data, accountSelection);
        JSONObject accountRow = selected.account;
        JSONArray rows = accountRow.optJSONArray(transactionKey(type));
        if (rows == null) return false;

        for (int i = 0; i < rows.length(); i++) {
            JSONObject row = rows.getJSONObject(i);
            if (row.getInt("id") == id) {
                BigDecimal amount = new BigDecimal(row.get("amount").toString());
                Account account = deserializeAccount(accountRow, selected.profile.getInt("id"));
                if (type == TransactionType.INCOME) account.withdraw(amount);
                else account.deposit(amount);
                rows.remove(i);
                accountRow.put("balance", account.getBalance().toPlainString());
                write(data);
                return true;
            }
        }
        return false;
    }

    private String selectionLabel(JSONObject profile, JSONObject account) {
        return profile.getString("name") + " — " + account.getString("name");
    }

    private AccountSelection findSelectedAccount(JSONObject data, String selection) {
        JSONArray profiles = data.getJSONArray("profiles");
        for (int i = 0; i < profiles.length(); i++) {
            JSONObject profile = profiles.getJSONObject(i);
            JSONArray accounts = profile.optJSONArray("accounts");
            if (accounts == null) continue;
            for (int j = 0; j < accounts.length(); j++) {
                JSONObject account = accounts.getJSONObject(j);
                if (selectionLabel(profile, account).equals(selection)) {
                    return new AccountSelection(profile, account);
                }
            }
        }
        throw new IllegalArgumentException("Account not found: " + selection);
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

    private static final class AccountSelection {
        private final JSONObject profile;
        private final JSONObject account;
        private AccountSelection(JSONObject profile, JSONObject account) {
            this.profile = profile;
            this.account = account;
        }
    }
}
