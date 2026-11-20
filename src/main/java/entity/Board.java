package entity;

/**
 * A simple entity representing the current Board state.
 * Boards store the current question, teams, and current number of wrong guesses.
 */
public class Board {
    private Question currentQuestion;
    private int activeTeam;
    private Team team1;
    private Team team2;
    private int wrongCount;

    /**
     * Creates a new Board  with the first question and both teams.
     * @param firstQuestion the first question to be put on the board
     * @param team1 the first team object
     * @param team2 the second team object
     */
    public Board(Question firstQuestion, Team team1, Team team2) {
        this.currentQuestion = firstQuestion;
        this.team1 = team1;
        this.team2 = team2;
        this.activeTeam = 1;
        this.wrongCount = 0;
    }

    public void setCurrentQuestion(Question newQuestion) {
        this.currentQuestion = newQuestion;
    }

    public Question getCurrentQuestion() {
        return this.currentQuestion;
    }

    /**
     * Returns the score associated with the provided answer, or 0 if the answer is not correct.
     * @param team the team to get
     * @return either team1 or team2 associated with the provided team
     * @throws IllegalArgumentException if team is not 1 or 2
     */
    public Team getTeam(int team) {
        if (team < 1 || team > 2) { throw new IllegalArgumentException("team must be 1 or 2"); }
        if (team == 1) { return this.team1; }
        return this.team2;
    }

    public Team getActiveTeam() {
        if (this.activeTeam == 1) { return this.team1; }
        return this.team2;
    }

    public int getWrongCount() {
        return this.wrongCount;
    }

    public void setWrongCount(int wrongCount) {
        this.wrongCount = wrongCount;
    }

    public void changeActiveTeam() {
        if (this.activeTeam == 1) { this.activeTeam = 2; }
        this.activeTeam = 1;
    }
}
