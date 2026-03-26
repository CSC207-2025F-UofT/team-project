package use_case.deleteAccount;

public class DeleteAccountOutputData {
    private final String username;

    public DeleteAccountOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
