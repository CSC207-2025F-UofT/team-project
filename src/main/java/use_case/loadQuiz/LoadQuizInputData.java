package use_case.loadQuiz;

public class LoadQuizInputData {
    private final int quizId;

    public LoadQuizInputData(int quizId) {
        this.quizId = quizId;
    }

    public int getQuizId() { return quizId; }
}
