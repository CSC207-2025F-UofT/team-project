package app;

import java.util.ArrayList;

public class MultiUserManager {
    private final ArrayList<Integer> loggedInUsers =
            new ArrayList<>();

    public void login(int userId) {
        if (!loggedInUsers.contains(userId)) {
            loggedInUsers.add(userId);
        }
    }

    public void logout(int userId) {
        loggedInUsers.remove(Integer.valueOf(userId));
    }

    public boolean isLoggedIn(int userId) {
        return loggedInUsers.contains(userId);
    }

    public boolean hasLoggedInUsers() {
        return !loggedInUsers.isEmpty();
    }
}
