package use_case.makeNewPlayer;

/**
 * The Input Data for Use Case 1.
 */
public class NewPlayerInputData {
    private final String username;

    public NewPlayerInputData(String username) {
        this.username = username;
    }

    String getUsername() {
        return username;
    }
}
