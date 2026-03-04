package use_case.quiz;

public interface SubmitQuizOutputBoundary {
    void present(SubmitQuizOutputData data);

    void switchToBattleView();
}