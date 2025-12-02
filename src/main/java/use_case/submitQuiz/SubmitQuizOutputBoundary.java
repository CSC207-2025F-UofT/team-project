package use_case.submitQuiz;

public interface SubmitQuizOutputBoundary {
    void present(SubmitQuizOutputData data);

    void switchToBattleView(boolean isCorrect);
}