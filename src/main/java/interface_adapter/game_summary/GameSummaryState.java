package interface_adapter.game_summary;

import java.util.ArrayList;
import java.util.List;

public class GameSummaryState {
    private String teamWon = "";
    private String teamLost = "";
    private int questionCount = 0;
    // Each row is one guess: round, question, team, guess, correct, points
    private List<String[]> rows = new ArrayList<>();

    public List<String[]> getRows() {
        return rows;
    }
    public void setRows(List<String[]> rows) {
        this.rows = rows;
    }
    public void addRow(String[] row) {
        this.rows.add(row);
    }

    public String getTeamWon() {
        return teamWon;
    }
    public void setTeamWon(String teamWon) {
        this.teamWon = teamWon;
    }
    public String getTeamLost() {
        return teamLost;
    }
    public void setTeamLost(String teamLost) {
        this.teamLost = teamLost;
    }
    public int getQuestionCount() {
        return questionCount;
    }
    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
}
