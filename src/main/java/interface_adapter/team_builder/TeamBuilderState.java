package interface_adapter.team_builder;

import entity.Team;

public class TeamBuilderState {
    private String teamNameError;

    private Team team = new Team("Team 1");

    public String getTeamNameError() {
        return teamNameError;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeamNameError(String teamNameError) {
        this.teamNameError = teamNameError;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
