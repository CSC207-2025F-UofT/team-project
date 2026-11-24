package use_case.account;

import entity.User;

import java.util.UUID;

public record AccountProfile(UUID userID, String username, String displayName) {
    public static AccountProfile from(User user) {
        return new AccountProfile(user.getUserID(), user.getUsername(), user.getDisplayName());
    }
}
