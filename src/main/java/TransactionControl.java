import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;

public class TransactionControl extends JSONControl {
  public TransactionControl(String file) throws IOException {
    super(file);
  }

  public JSONObject returnTransactions(int userID) {
    return getContents().getJSONObject(String.valueOf(userID));
  }

  public int nextID(int userID) throws IOException {
    if (returnTransactions(userID).keySet().isEmpty()) {
      return 0;
    } else {
      return returnTransactions(userID).keySet().stream().map(Integer::parseInt).max(Integer::compareTo).get() + 1;
    }
  }

  public int  writeTransactions(Account user, double amount, LocalDate date, String description, Category category,
                                String note, String transactionCurrency) throws IOException {
    JSONObject transactions = returnTransactions(user.getAccountID());
    String accountCurrency = user.getCurrency();
    String categoryName = category.getName();
    String transactionTime = date.toString();

    JSONArray transactionRecord = new JSONArray();
    int transactionID = nextID(user.getAccountID());

    transactionRecord.put(categoryName);
    transactionRecord.put(transactionTime);
    transactionRecord.put(note);
    transactionRecord.put(description);
    transactionRecord.put(transactionCurrency);
    transactionRecord.put(amount);

    if (transactionCurrency.equals(accountCurrency)) {
      transactionRecord.put(amount);
    } else {
      String currencyDatabase = "Currencies.json";
      CurrencyControl converter = new CurrencyControl(currencyDatabase);
      double balance = converter.exchange(transactionCurrency, accountCurrency, amount);
      transactionRecord.put(balance);
    }
    transactions.put(String.valueOf(transactionID), transactionRecord);
    writeContents();
    return transactionID;
  }

  public void addAccount (String accountID) throws IOException {
    getContents().put(accountID, new JSONObject());
    writeContents();
  }
}
