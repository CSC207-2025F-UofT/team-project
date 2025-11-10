package use_case.grade_team;

import entity.Team;

public interface GradingStrategy {
    public float gradeTeam(Team team);
}
