package frameworks_and_drivers.persistence;

import application_business_rules.create_account.AccountDataAccessInterface;
import entity.Account;
import entity.AccountType;
import entity.UserProfile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

/** JSON implementation of the Create Account use case's Data Access Interface. */
public final class JsonAccountDataAccess implements AccountDataAccessInterface {
    private final Path filePath;

    public JsonAccountDataAccess(Path filePath) {
        this.filePath = filePath;
    }

    private JSONObject read() {
        try {
            return new JSONObject(Files.readString(filePath));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read profile data.", exception);
        }
    }

    private void write(JSONObject data) {
        try {
            Path parent = filePath.getParent();
            if (parent != null) Files.createDirectories(parent);
            Files.writeString(filePath, data.toString(2));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to save profile data.", exception);
        }
    }

    @Override
    public UserProfile getUserProfile(int userProfileId) {
        JSONArray profiles = read().getJSONArray("profiles");
        for (int i = 0; i < profiles.length(); i++) {
            JSONObject row = profiles.getJSONObject(i);
            if (row.getInt("id") == userProfileId) {
                UserProfile profile = new UserProfile(row.getInt("id"),
                    row.getString("name"), row.getString("email"),
                    row.getString("passwordHash"));
                JSONArray accounts = row.optJSONArray("accounts");
                if (accounts != null) {
                    for (int j = 0; j < accounts.length(); j++) {
                        JSONObject accountRow = accounts.getJSONObject(j);
                        Account account = new Account(accountRow.getInt("id"),
                            userProfileId, accountRow.getString("name"),
                            AccountType.valueOf(accountRow.getString("type")),
                            new BigDecimal(accountRow.getString("balance")),
                            accountRow.getString("currency"),
                            LocalDateTime.parse(accountRow.getString("createdAt")));
                        profile.addAccount(account);
                    }
                }
                return profile;
            }
        }
        return null;
    }

    @Override
    public boolean accountNameExists(int userProfileId, String accountName) {
        UserProfile profile = getUserProfile(userProfileId);
        if (profile == null) return false;
        return profile.getAccounts().stream()
            .anyMatch(account -> account.getAccountName().equalsIgnoreCase(accountName.trim()));
    }

    @Override
    public int nextAccountId() {
        return read().getInt("nextAccountId");
    }

    @Override
    public void saveAccount(Account account, UserProfile updatedProfile) {
        JSONObject data = read();
        JSONArray profiles = data.getJSONArray("profiles");
        boolean found = false;

        for (int i = 0; i < profiles.length(); i++) {
            JSONObject profileRow = profiles.getJSONObject(i);
            if (profileRow.getInt("id") == updatedProfile.getId()) {
                JSONArray accounts = new JSONArray();
                for (Account profileAccount : updatedProfile.getAccounts()) {
                    JSONObject accountRow = new JSONObject();
                    accountRow.put("id", profileAccount.getAccountID());
                    accountRow.put("name", profileAccount.getAccountName());
                    accountRow.put("type", profileAccount.getAccountType().name());
                    accountRow.put("balance", profileAccount.getBalance().toPlainString());
                    accountRow.put("currency", profileAccount.getCurrency());
                    accountRow.put("createdAt", profileAccount.getCreatedAt().toString());
                    accounts.put(accountRow);
                }
                profileRow.put("accounts", accounts);
                found = true;
                break;
            }
        }

        if (!found) throw new IllegalArgumentException("User profile could not be found.");
        data.put("nextAccountId", Math.max(data.getInt("nextAccountId"), account.getAccountID() + 1));
        write(data);
    }
}
