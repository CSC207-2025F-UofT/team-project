package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** A registered person. Financial accounts belong to the profile. */
public final class UserProfile {
    private final int userID;
    private String name;
    private final String email;
    /** A password hash, when authentication has been implemented for this profile. */
    private final String passwordHash;
    private final LocalDateTime createdAt;
    private final List<Account> accounts = new ArrayList<>();

    /** Creates a profile for existing/demo data that has no authentication credentials yet. */
    public UserProfile(int id, String name, LocalDateTime createdAt) {
        this(id, name, null, null, createdAt);
    }

    public UserProfile(int id, String name, String email, String passwordHash) {
        this(id, name, email, passwordHash, LocalDateTime.now());
    }

    public UserProfile(int id, String name, String email, String passwordHash, LocalDateTime createdAt) {
        if (id < 0) throw new IllegalArgumentException("User ID cannot be negative.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty.");
        this.userID = id;
        this.name = name.trim();
        this.email = email == null || email.isBlank() ? null : email.trim().toLowerCase();
        this.passwordHash = passwordHash == null || passwordHash.isBlank() ? null : passwordHash;
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public int getId() { return userID; }
    public int getUserID() { return userID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getNumAccounts() { return accounts.size(); }
    public List<Account> getAccounts() { return Collections.unmodifiableList(accounts); }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) throw new IllegalArgumentException("Name cannot be empty.");
        name = newName.trim();
    }

    public void addAccount(Account account) {
        if (account == null) throw new IllegalArgumentException("Account is required.");
        if (account.getOwnerProfileId() != userID) throw new IllegalArgumentException("This account belongs to another profile.");
        if (accounts.stream().anyMatch(a -> a.getAccountID() == account.getAccountID()))
            throw new IllegalArgumentException("Account has already been added.");
        accounts.add(account);
    }

    public void removeAccount(int id) { accounts.removeIf(a -> a.getAccountID() == id); }
}
