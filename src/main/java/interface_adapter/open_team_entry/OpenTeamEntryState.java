package interface_adapter.open_team_entry;

public class OpenTeamEntryState {
    private String[] players;
    private String errorMessage;

    /**
     * Initializes the state with empty player fields.
     */
    public OpenTeamEntryState() {
        this.players = new String[15];
        for (int i = 0; i < players.length; i++) {
            players[i] = "";
        }
    }

    public String[] getPlayers() { return players; }

    public String getErrorMessage() { return errorMessage; }

    public void setPlayers(String[] players) { this.players = players; }

    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("OpenTeamEntryState{ players=[");
        if (players != null) {
            for (int i = 0; i < players.length; i++) {
                sb.append(players[i]);
                if (i < players.length - 1) sb.append(", ");
            }
        }
        sb.append("], errorMessage='").append(errorMessage).append("' }");
        return sb.toString();
    }
}
