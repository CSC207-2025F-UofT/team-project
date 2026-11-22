package application_business_rules.create_account;

import entity.Account;
import entity.UserProfile;

/** Use-case-owned data access contract, implemented by an outer-layer gateway. */
public interface AccountDataAccessInterface {
    UserProfile getUserProfile(int userProfileId);
    boolean accountNameExists(int userProfileId, String accountName);
    int nextAccountId();

    /** Persists the new account and updated profile association as one operation. */
    void saveAccount(Account account, UserProfile updatedProfile);
}
