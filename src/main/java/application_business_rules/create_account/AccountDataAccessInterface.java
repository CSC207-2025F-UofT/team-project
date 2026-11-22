package application_business_rules.create_account;

import entity.Account;
import entity.UserProfile;

/**
 * Use-case-owned data access contract. The concrete JSON/database gateway
 * belongs in the Frameworks and Drivers layer and implements this interface.
 */
public interface AccountDataAccessInterface {
    UserProfile getUserProfile(int userProfileId);
    boolean accountNameExists(int userProfileId, String accountName);
    int nextAccountId();
    void saveAccount(Account account);
    void saveUserProfile(UserProfile userProfile);
}
