package interface_adapter.lobby;

/**
 * The state for the lobby: who is logged in.
 */
public class LobbyState {

    private String username = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
