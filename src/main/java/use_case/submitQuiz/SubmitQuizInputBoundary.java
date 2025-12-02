package use_case.submitQuiz;

public interface SubmitQuizInputBoundary {
    void submit(SubmitQuizInputData data);

    void switchToBattleView(boolean isCorrect);
}
