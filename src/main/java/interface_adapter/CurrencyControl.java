package interface_adapter;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.time.LocalDate;
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

  public void refreshCurrency(LocalDate date) throws IOException, InterruptedException {
    Set<String> currencyCodes = returnCurrencies().keySet();
    JSONObject currencies = returnCurrencies();

    for (String currency : currencyCodes) {
      if (!(currency.equals("CAD"))) {
      try{
        CurrencyAPI metric = new CurrencyAPI(currency, date);
        currencies.put(currency, metric.getResponse());}
      catch (JSONException ignored) {
      }
      }
      }
    writeContents();
    }

  public String returnCurrency(int userID) {
    return getContents().getJSONArray("defaultCurrency").getString(userID);
  }
}
