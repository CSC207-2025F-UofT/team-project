package use_case.quiz;

public interface SubmitQuizInputBoundary {
    void submit(SubmitQuizInputData data);

    void switchToBattleView();
}
