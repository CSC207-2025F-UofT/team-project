package entity;

import java.time.LocalDateTime;
import java.util.List;

public class GameRecord {
    private final Team team1;
    private final Team team2;
    private final List<RoundRecord> rounds;
    private final LocalDateTime playedAt;

    public GameRecord(Team team1, Team team2, List<RoundRecord> rounds) {
        this.team1 = team1;
        this.team2 = team2;
        this.rounds = rounds;
        this.playedAt = LocalDateTime.now();
    }

    public Team getTeam1() {
        return team1;
    }
    public Team getTeam2() {
        return team2;
    }
    public List<RoundRecord> getRounds() {
        return rounds;
    }
    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    public int getTeamScore(Team team) {
        return team.getScore();
    }

    public int getTotalPoints() {
        return getTeamScore(team1) + getTeamScore(team2);
    }

    public String getWinner() {
        int team1Score = getTeamScore(team1);
        int team2Score = getTeamScore(team2);
        if (team1Score > team2Score) {
            return team1.getName();
        }
        if (team2Score > team1Score) {
            return team2.getName();
        }
        return null;
    }
}