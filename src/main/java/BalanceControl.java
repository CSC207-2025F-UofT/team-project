import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

public class BalanceControl extends JSONControl {
  public BalanceControl(String file) throws IOException {
    super(file);
  }

  public JSONArray returnBalances() {
    return getContents().getJSONArray("balanceTracking");
  }

  public void initiateBalance(double newBalance) throws IOException {
    JSONArray balances = returnBalances();
    balances.put(newBalance);
    writeContents();
  }
  public void updateBalance(int userID, double newBalance) throws IOException {
    JSONArray balances = returnBalances();
    balances.put(userID, newBalance);
    writeContents();
  }

  public double returnBalance(int userID) throws IOException {
    return returnBalances().getDouble(userID);
}}
