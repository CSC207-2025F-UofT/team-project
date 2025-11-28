package use_case.createAccount;

public class CreateAccountOutputData {

    private final String username;

    public CreateAccountOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
