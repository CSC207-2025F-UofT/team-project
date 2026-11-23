package use_case.enter_name.makeNewPlayer;

public class NewPlayerOutputData {
    private final String username;

    public NewPlayerOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
