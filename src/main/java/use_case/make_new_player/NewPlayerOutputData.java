package use_case.make_new_player;

public class NewPlayerOutputData {
    private final String username;

    public NewPlayerOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
