package entity;

public abstract class Question {
    private final String questionText;
    private final int difficultyLevel;

    public Question(String questionText, int difficultyLevel) {
        this.questionText = questionText;
        this.difficultyLevel = difficultyLevel;
    }

    public abstract boolean checkAnswer(String userAnswer);

    public String getQuestionText() { return questionText; }
    public int getDifficultyLevel() { return difficultyLevel; }
}
