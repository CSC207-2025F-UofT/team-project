package use_case.track_score;

public class TrackScoreInputData {
    private final String teamOneName;
    private final String teamTwoName;
    private final boolean gameEnded;

    public TrackScoreInputData(String teamOneName, String teamTwoName, boolean gameEnded) {
        this.teamOneName = teamOneName;
        this.teamTwoName = teamTwoName;
        this.gameEnded = gameEnded;
    }

    public String getTeamOneName() { return teamOneName; }

    public String getTeamTwoName() { return teamTwoName; }

    public boolean isGameEnded() { return gameEnded; }
}