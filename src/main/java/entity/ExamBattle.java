package entity;

public class ExamBattle {
    private static final int STARTING_HP = 60;
    private static final int DAMAGE = 20;
    private static final int MAX_QUESTIONS = 5;

    private int playerHp = STARTING_HP;
    private int bossHp = STARTING_HP;
    private int answeredCount;
    private int correctCount;

    public void submitAnswer(boolean correct) {
        if (isFinished()) {
            throw new IllegalStateException("This battle has already ended.");
        }

        answeredCount++;
        if (correct) {
            bossHp -= DAMAGE;
            correctCount++;
        } else {
            playerHp -= DAMAGE;
        }
    }

    public boolean isFinished() {
        return bossHp == 0 || playerHp == 0 || answeredCount >= MAX_QUESTIONS;
    }

    public boolean hasWon() {
        return bossHp == 0;
    }

    public int getPlayerHp() {
        return playerHp;
    }

    public int getBossHp() {
        return bossHp;
    }

    public int getAnsweredCount() {
        return answeredCount;
    }

    public int getCorrectCount() {
        return correctCount;
    }
}
