package entity;

public class Question {
    public final String questionText;
    public final String answerText;
    public final int difficultyLevel;
    public Question(String questionText, String answerText, int difficultyLevel) {
        this.questionText = questionText;
        this.answerText = answerText;
        this.difficultyLevel = difficultyLevel;
    }


}
