package interface_adapter.team_builder;

import entity.Team;

public class TeamBuilderState {
    private String teamName = "";
    private String teamNameError;

    private Team team = new Team("Team 1");

    public String getTeamName() {
        return teamName;
    }

    public String getTeamNameError() {
        return teamNameError;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setTeamNameError(String teamNameError) {
        this.teamNameError = teamNameError;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
