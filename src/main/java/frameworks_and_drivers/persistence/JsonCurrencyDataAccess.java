package frameworks_and_drivers.persistence;

import use_case.change_currency.CurrencyDataAccessInterface;
import entity.Account;
import entity.AccountType;
import entity.SavingsAccount;
import entity.ChequingAccount;
import entity.CashAccount;
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
                    return deserializeAccount(row, profile.getInt("id"));
                }
            }
        }
        return null;
    }

    /**
     * Reconstructs the concrete domain subtype so its account-specific rules
     * (interest, overdraft limit, or cash location) are preserved.
     */
    private Account deserializeAccount(JSONObject row, int ownerProfileId) {
        int id = row.getInt("id");
        String name = row.getString("name");
        BigDecimal balance = new BigDecimal(row.getString("balance"));
        String currency = row.getString("currency");
        LocalDateTime createdAt = LocalDateTime.parse(row.getString("createdAt"));

        AccountType type = AccountType.valueOf(row.getString("type"));
        switch (type) {
            case SAVINGS:
                return new SavingsAccount(id, ownerProfileId, name, balance, currency,
                    new BigDecimal(row.optString("annualInterestRate", "0")), createdAt);
            case CHEQUING:
                return new ChequingAccount(id, ownerProfileId, name, balance, currency,
                    new BigDecimal(row.optString("overdraftLimit", "0")), createdAt);
            case CASH:
                return new CashAccount(id, ownerProfileId, name, balance, currency,
                    row.optString("storageLocation", "Wallet"), createdAt);
            default:
                throw new IllegalArgumentException("Unsupported account type: " + type);
        }
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
