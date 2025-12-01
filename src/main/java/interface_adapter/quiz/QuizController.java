package interface_adapter.quiz;

import use_case.loadQuiz.LoadQuizInputBoundary;
import use_case.loadQuiz.LoadQuizInputData;
import use_case.quiz.SubmitQuizInputBoundary;
import use_case.quiz.SubmitQuizInputData;

public class QuizController {
    private final SubmitQuizInputBoundary submitInteractor;
    private final LoadQuizInputBoundary loadInteractor;

    public QuizController(SubmitQuizInputBoundary submitInteractor, LoadQuizInputBoundary loadInteractor) {
        this.submitInteractor = submitInteractor;
        this.loadInteractor = loadInteractor;
    }

    public void loadQuiz(int quizId) {
        loadInteractor.loadQuiz(new LoadQuizInputData(quizId));
    }

    /**
     * @param selectedOptionIdOrNull  pass null if user didn't pick an option
     */
    public void onSubmit(int quizId, Integer selectedOptionIdOrNull) {
        SubmitQuizInputData input = new SubmitQuizInputData(quizId, selectedOptionIdOrNull);
        submitInteractor.submit(input);
    }

    public void switchToBattleView() {
        submitInteractor.switchToBattleView();
    }
}