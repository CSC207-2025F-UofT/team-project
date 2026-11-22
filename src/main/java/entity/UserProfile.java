package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A person's profile. A profile may own multiple financial Accounts. */
public final class UserProfile {
    private final int userID;
    private String name;
    private final String email;
    private final String passwordHash;
    private final List<Account> accounts = new ArrayList<>();

    public UserProfile(int userID, String name, String email, String passwordHash) {
        if (userID < 0) throw new IllegalArgumentException("User ID cannot be negative.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty.");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email cannot be empty.");
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("A password hash is required.");
        }
        this.userID = userID;
        this.name = name.trim();
        this.email = email.trim();
        this.passwordHash = passwordHash;
    }

    public int getId() { return userID; }
    public int getUserID() { return userID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public int getNumAccounts() { return accounts.size(); }
    public List<Account> getAccounts() { return Collections.unmodifiableList(accounts); }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        name = newName.trim();
    }

    public void addAccount(Account account) {
        if (account == null) throw new IllegalArgumentException("Account is required.");
        if (account.getOwnerProfileId() != userID) {
            throw new IllegalArgumentException("This account belongs to another profile.");
        }
        for (Account existing : accounts) {
            if (existing.getAccountID() == account.getAccountID()) {
                throw new IllegalArgumentException("Account has already been added.");
            }
        }
        accounts.add(account);
    }

    public void removeAccount(int accountId) {
        accounts.removeIf(account -> account.getAccountID() == accountId);
    }
}
