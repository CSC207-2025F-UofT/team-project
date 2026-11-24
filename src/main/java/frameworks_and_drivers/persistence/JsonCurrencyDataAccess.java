package frameworks_and_drivers.persistence;

import use_case.change_currency.CurrencyDataAccessInterface;
import entity.Account;
import entity.AccountType;
import frameworks_and_drivers.currency.BankOfCanadaCurrencyClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** JSON account persistence combined with the outer-layer Bank of Canada rate client. */
public final class JsonCurrencyDataAccess implements CurrencyDataAccessInterface {
    private final Path filePath;
    private final BankOfCanadaCurrencyClient currencyClient;

    public JsonCurrencyDataAccess(Path filePath, BankOfCanadaCurrencyClient currencyClient) {
        this.filePath = filePath;
        this.currencyClient = currencyClient;
    }

    private JSONObject read() {
        try {
            return new JSONObject(Files.readString(filePath));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read account data.", exception);
        }
    }

    private void write(JSONObject data) {
        try {
            Path parent = filePath.getParent();
            if (parent != null) Files.createDirectories(parent);
            Files.writeString(filePath, data.toString(2));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to save account data.", exception);
        }
    }

    @Override
    public Account getAccount(int accountId) {
        JSONArray profiles = read().getJSONArray("profiles");
        for (int i = 0; i < profiles.length(); i++) {
            JSONObject profile = profiles.getJSONObject(i);
            JSONArray accounts = profile.optJSONArray("accounts");
            if (accounts == null) continue;
            for (int j = 0; j < accounts.length(); j++) {
                JSONObject row = accounts.getJSONObject(j);
                if (row.getInt("id") == accountId) {
                    return new Account(accountId, profile.getInt("id"),
                        row.getString("name"), AccountType.valueOf(row.getString("type")),
                        new BigDecimal(row.getString("balance")), row.getString("currency"),
                        LocalDateTime.parse(row.getString("createdAt")));
                }
            }
        }
        return null;
    }

    @Override
    public BigDecimal getExchangeRate(String sourceCurrency, String targetCurrency, LocalDate date)
            throws IOException, InterruptedException {
        if (sourceCurrency.equals(targetCurrency)) return BigDecimal.ONE;
        BigDecimal sourceCad = currencyClient.getCadPerUnit(sourceCurrency, date);
        BigDecimal targetCad = currencyClient.getCadPerUnit(targetCurrency, date);
        return sourceCad.divide(targetCad, 10, RoundingMode.HALF_UP);
    }

    @Override
    public void saveAccount(Account account) {
        JSONObject data = read();
        JSONArray profiles = data.getJSONArray("profiles");
        boolean found = false;
        for (int i = 0; i < profiles.length() && !found; i++) {
            JSONObject profile = profiles.getJSONObject(i);
            JSONArray accounts = profile.optJSONArray("accounts");
            if (accounts == null) continue;
            for (int j = 0; j < accounts.length(); j++) {
                JSONObject row = accounts.getJSONObject(j);
                if (row.getInt("id") == account.getAccountID()) {
                    row.put("balance", account.getBalance().toPlainString());
                    row.put("currency", account.getCurrency());
                    found = true;
                    break;
                }
            }
        }
        if (!found) throw new IllegalArgumentException("Account could not be found.");
        write(data);
    }
}
