package use_case.track_score;

/**
 * Output Data for the Track Score use case.
 */
public class TrackScoreOutputData {
    private final String teamOneName;
    private final int teamOneScore;
    private final String teamTwoName;
    private final int teamTwoScore;
    private final boolean gameEnded;

    public TrackScoreOutputData(String teamOneName, int teamOneScore,
                                String teamTwoName, int teamTwoScore,
                                boolean gameEnded) {
        this.teamOneName = teamOneName;
        this.teamOneScore = teamOneScore;
        this.teamTwoName = teamTwoName;
        this.teamTwoScore = teamTwoScore;
        this.gameEnded = gameEnded;
    }

    public String getTeamOneName() { return teamOneName; }

    public int getTeamOneScore() { return teamOneScore; }

    public String getTeamTwoName() { return teamTwoName; }

    public int getTeamTwoScore() { return teamTwoScore; }

    public boolean isGameEnded() { return gameEnded; }
}