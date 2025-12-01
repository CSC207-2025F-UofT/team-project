package use_case.loadQuiz;

import java.util.List;

public class LoadQuizOutputData {
    private final int quizId;
    private final String questionText;
    private final List<String> optionTexts;

    public LoadQuizOutputData(int quizId, String questionText, List<String> optionTexts) {
        this.quizId = quizId;
        this.questionText = questionText;
        this.optionTexts = optionTexts;
    }

    public int getQuizId() {
        return quizId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptionTexts() {
        return optionTexts;
    }
}
