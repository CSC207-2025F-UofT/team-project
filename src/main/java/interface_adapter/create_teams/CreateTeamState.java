package interface_adapter.create_teams;

/**
 * The state information representing the main menu
 */
public class MainMenuState {
    private String team1Name = "";
    private String team2Name = "";
    private String startGameError;

    public String getTeam1Name() {
        return team1Name;
    }

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }

    public String getTeam2Name() {
        return this.team2Name;
    }

    public void setTeam2Name(String team1Name) {
        this.team2Name = team1Name;
    }

    public String getStartGameError() {
        return startGameError;
    }

    public void setLoadQuestionError(String startGameError) {
        this.startGameError = startGameError;
    }
}
