package use_case.change_username;

public class ChangeUsernameInputData {
    private final String currentUsername;
    private final String newUsername;

    public ChangeUsernameInputData(String currentUsername, String newUsername) {
        this.currentUsername = currentUsername;
        this.newUsername = newUsername;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public String getNewUsername() {
        return newUsername;
    }
}