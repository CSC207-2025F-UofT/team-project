import org.json.JSONObject;
import java.io.IOException;
import java.util.Set;

public class CurrencyControl extends JSONControl {

  public CurrencyControl(String file) throws IOException {
    super(file);
  }

  public JSONObject returnCurrencies() {
    return getContents();
  }

  public boolean validCurrency (String userInput) {
    Set<String> currencyCodes = returnCurrencies().keySet();
    return currencyCodes.contains(userInput);
  }

  public double getExchangeRate (String currency) {
    JSONObject currencies = returnCurrencies();
    return currencies.getDouble(currency);
  }

  public double exchange (String baseCurrency, String depositCurrency, double amount) {
    double baseExchange = getExchangeRate(baseCurrency);
    double depositExchange = getExchangeRate(depositCurrency);
    return amount * (depositExchange / baseExchange);
  }

  public void addCurrency(String currency) throws IOException{
    getContents().getJSONArray("defaultCurrency").put(currency);
    writeContents();
  }

  public void changeCurrency(int userID, String newCurrency) throws IOException {
    getContents().getJSONArray("defaultCurrency").put(userID, newCurrency);
    writeContents();
  }

  public String returnCurrency(int userID) {
    return getContents().getJSONArray("defaultCurrency").getString(userID);
  }
}
