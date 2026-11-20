package use_case.question_input;

import entity.Question;

public class QuestionInputInputData {
    private final String guessText;

    public QuestionInputInputData(String guessText) {
        this.guessText = guessText;
    }

    public String getGuessText() {
        return guessText;
    }
}
