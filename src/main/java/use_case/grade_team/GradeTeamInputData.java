package use_case.grade_team;

import entity.Team;
import entity.GradingStrategy;

public class GradeTeamInputData {

    private final Team team;
    private final GradingStrategy strategy;

    public GradeTeamInputData(Team team, GradingStrategy strategy) {
        this.team = team;
        this.strategy = strategy;
    }

    public Team getTeam() {return team;}

    public GradingStrategy getStrategy() {
        return strategy;
    }
}
