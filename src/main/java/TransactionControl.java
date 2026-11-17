
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class TransactionControl extends JSONControl {

    public TransactionControl(String file) throws IOException {
        super(file);
    }
    // Retrieves all usernames from the JSON file
    public List<String> getUsers() {

        JSONObject users = getContents().getJSONObject("users");

        return new ArrayList<>(users.keySet());
    }

    public JSONArray getIncome(String username) {

        return getContents()
                .getJSONObject("users")
                .getJSONObject(username)
                .getJSONArray("income");
    }

    public JSONArray getExpenses(String username) {

        return getContents()
                .getJSONObject("users")
                .getJSONObject(username)
                .getJSONArray("expenses");
    }

    public void addTransaction(String username, String type,
                               double amount, String description,
                               String date) throws IOException {

        JSONObject user = getContents()
                .getJSONObject("users")
                .getJSONObject(username);

        JSONArray transactions = user.getJSONArray(type);

        // Find the next available ID
        int nextId = 1;

        for (int i = 0; i < transactions.length(); i++) {
            int existingId = transactions.getJSONObject(i).getInt("id");
            nextId = Math.max(nextId, existingId + 1);
        }

        JSONObject newTransaction = new JSONObject();

        newTransaction.put("id", nextId);
        newTransaction.put("amount", amount);
        newTransaction.put("description", description);
        newTransaction.put("date", date);

        transactions.put(newTransaction);

        writeContents();
    }

    // Delete a transaction using its ID
    public boolean deleteTransaction(String username, String type,
                                     int transactionId) throws IOException {

        JSONObject user = getContents()
                .getJSONObject("users")
                .getJSONObject(username);

        JSONArray transactions = user.getJSONArray(type);

        for (int i = 0; i < transactions.length(); i++) {

            JSONObject transaction = transactions.getJSONObject(i);

            if (transaction.getInt("id") == transactionId) {

                transactions.remove(i);

                writeContents();

                return true;
            }
        }

        return false;
    }
}
