package entity;

public abstract class Question {
    public final String questionText;
    public final int difficultyLevel;

    public Question(String questionText, int difficultyLevel) {
        this.questionText = questionText;
        this.difficultyLevel = difficultyLevel;
    }

    public abstract boolean checkAnswer(String userAnswer);

    public String getQuestionText() { return questionText; }
    public int getDifficultyLevel() { return difficultyLevel; }
}
