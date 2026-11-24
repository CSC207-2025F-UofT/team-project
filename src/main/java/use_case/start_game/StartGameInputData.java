package use_case.start_game;

public class StartGameInputData {
    private final String team1Name;
    private final String team2Name;

    public StartGameInputData(String team1Name, String team2Name) {
        this.team1Name = team1Name;
        this.team2Name = team2Name;
    }

    public String getTeam1Name() { return this.team1Name; }

    public String getTeam2Name() { return this.team2Name; }
}
