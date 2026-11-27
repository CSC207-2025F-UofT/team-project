package use_case.start_game;

import entity.Team;

/**
 * Output Data for the Start Game Use Case.
 */
public class StartGameOutputData {
    private final String team1Name;
    private final String team2Name;

    StartGameOutputData(String team1Name, String team2Name) {
        this.team1Name = team1Name;
        this.team2Name = team2Name;
    }

    public String getTeam1Name() { return this.team1Name; }

    public String getTeam2Name() { return this.team2Name; }
}
