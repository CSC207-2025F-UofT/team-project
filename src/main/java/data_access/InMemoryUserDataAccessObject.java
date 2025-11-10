package data_access;

import entity.Team;
import use_case.grade_team.GradeTeamUserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserDataAccessObject implements GradeTeamUserDataAccessInterface {

    private final Map<String, Team> teams = new HashMap<>();

    public void saveTeam(Team team) {
        teams.put(team.getTeamName(), team);
    }

    @Override
    public Team getTeam(String teamName) {
        return teams.get(teamName);
    }
}
