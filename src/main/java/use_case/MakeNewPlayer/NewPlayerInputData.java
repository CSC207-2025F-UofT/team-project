package use_case.MakeNewPlayer;

/**
 * The Input Data for Use Case 1.
 */
public class NewPlayerInputData {
    private final String username;

    public NewPlayerInputData(String username, String password, String repeatPassword) {
        this.username = username;
    }

    String getUsername() {
        return username;
    }
}
