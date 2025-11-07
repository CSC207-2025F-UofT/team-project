package quiz_system.usecase;

public class SubmitQuizInputData {
    private final int quizId;
    private final Integer selectedOptionId; // may be null if user didn't select

    public SubmitQuizInputData(int quizId, Integer selectedOptionId) {
        this.quizId = quizId;
        this.selectedOptionId = selectedOptionId;
    }

    public int getQuizId() {
        return quizId;
    }

    public Integer getSelectedOptionId() {
        return selectedOptionId;
    }
}
