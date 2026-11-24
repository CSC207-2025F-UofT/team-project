package entity;

public class GuessRecord {
    private final String teamName;
    private final String questionText;
    private final String guess;
    private final boolean correct;
    private final int pointsAwarded;

    public GuessRecord(String teamName, String questionText, String guess, boolean correct, int pointsAwarded) {
        this.teamName = teamName;
        this.questionText = questionText;
        this.guess = guess;
        this.correct = correct;
        this.pointsAwarded = pointsAwarded;
    }

    public String getTeamName() {
        return teamName;
    }
    public String getQuestionText() {
        return questionText;
    }
    public String getGuess() {
        return guess;
    }
    public boolean isCorrect() {
        return correct;
    }
    public int getPointsAwarded() {
        return pointsAwarded;
    }
}

