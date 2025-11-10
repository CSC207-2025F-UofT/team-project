//Entity for multiplayer gameplay
//Mahir
package entity;

import java.util.List;

public class MultiPlayerGame {
    private final Player playerA;
    private final Player playerB;
    private final List<MultipleChoiceQuestion> questions;

    private int currentQuestionIndex;
    private Player currentTurn;
    private boolean isFinished;

    private int scoreA;
    private int scoreB;

    public MultiPlayerGame(Player playerA, Player playerB, List<MultipleChoiceQuestion> questions) {
        this.playerA = playerA;
        this.playerB = playerB;
        this.questions = questions;
        this.currentQuestionIndex = 0;
        this.currentTurn = playerA;
        this.isFinished = false;
        this.scoreA = 0;
        this.scoreB = 0;
    }


    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public List<MultipleChoiceQuestion> getQuestions() {
        return questions;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int index) {
        this.currentQuestionIndex = index;
    }

    public Player getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(Player currentTurn) {
        this.currentTurn = currentTurn;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        this.isFinished = finished;
    }

    public int getScoreA() {
        return scoreA;
    }

    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    public int getScoreB() {
        return scoreB;
    }

    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }
}
