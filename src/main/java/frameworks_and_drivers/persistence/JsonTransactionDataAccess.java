package frameworks_and_drivers.persistence;

import use_case.transactions.TransactionDataAccessInterface;
import entity.Transaction;
import entity.TransactionType;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/** Concrete JSON gateway for the dashboard's existing username-based data file. */
public final class JsonTransactionDataAccess implements TransactionDataAccessInterface {
    private final Path filePath;
    public JsonTransactionDataAccess(Path filePath) { this.filePath = filePath; }

    private JSONObject read() throws IOException {
        if (!Files.exists(filePath)) throw new IOException("Missing transaction data: " + filePath);
        return new JSONObject(Files.readString(filePath));
    }
    private void write(JSONObject data) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) Files.createDirectories(parent);
        Files.writeString(filePath, data.toString(2));
    }
    @Override
    public List<String> getUserNames() throws IOException {
        List<String> names = new ArrayList<>(read().getJSONObject("users").keySet());
        Collections.sort(names);
        return names;
    }
    @Override
    public List<Transaction> getTransactions(String userName, TransactionType type) throws IOException {
        JSONObject users = read().getJSONObject("users");
        if (!users.has(userName)) throw new IllegalArgumentException("User not found: " + userName);
        String key = type == TransactionType.INCOME ? "income" : "expenses";
        JSONArray rows = users.getJSONObject(userName).getJSONArray(key);
        List<Transaction> result = new ArrayList<>();
        for (int i = 0; i < rows.length(); i++) {
            JSONObject row = rows.getJSONObject(i);
            result.add(new Transaction(row.getInt("id"), type,
                new BigDecimal(row.get("amount").toString()), row.getString("description"),
                LocalDate.parse(row.getString("date"))));
        }
        return result;
    }
    @Override
    public void addTransaction(String userName, Transaction transaction) throws IOException {
        JSONObject data = read();
        JSONObject users = data.getJSONObject("users");
        if (!users.has(userName)) throw new IllegalArgumentException("User not found: " + userName);
        String key = transaction.getType() == TransactionType.INCOME ? "income" : "expenses";
        JSONArray rows = users.getJSONObject(userName).getJSONArray(key);
        int nextId = 1;
        for (int i = 0; i < rows.length(); i++) nextId = Math.max(nextId, rows.getJSONObject(i).getInt("id") + 1);
        JSONObject row = new JSONObject();
        row.put("id", nextId);
        row.put("amount", transaction.getAmount());
        row.put("description", transaction.getDescription());
        row.put("date", transaction.getDate().toString());
        rows.put(row);
        write(data);
    }
    @Override
    public boolean deleteTransaction(String userName, TransactionType type, int id) throws IOException {
        JSONObject data = read();
        JSONObject users = data.getJSONObject("users");
        if (!users.has(userName)) throw new IllegalArgumentException("User not found: " + userName);
        String key = type == TransactionType.INCOME ? "income" : "expenses";
        JSONArray rows = users.getJSONObject(userName).getJSONArray(key);
        for (int i = 0; i < rows.length(); i++) {
            if (rows.getJSONObject(i).getInt("id") == id) {
                rows.remove(i);
                write(data);
                return true;
            }
        }
        return false;
    }
}
