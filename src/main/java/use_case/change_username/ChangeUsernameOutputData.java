package use_case.change_username;

public class ChangeUsernameOutputData {
    private final String newUsername;

    public ChangeUsernameOutputData(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getNewUsername() {
        return newUsername;
    }
}