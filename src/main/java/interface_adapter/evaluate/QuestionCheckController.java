package interface_adapter.evaluate;

import use_case.evaluate.QuestionCheckInputBoundary;

public class QuestionCheckController {
    private final QuestionCheckInputBoundary interactor;

    public QuestionCheckController(QuestionCheckInputBoundary interactor) {
        this.interactor = interactor;
    }

    public boolean isQuestion(String latex) {
        return interactor.isQuestion(latex);
    }
}
