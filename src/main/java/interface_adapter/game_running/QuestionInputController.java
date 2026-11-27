package interface_adapter.game_running;

import use_case.question_input.QuestionInputInputBoundary;
import use_case.question_input.QuestionInputInputData;

public class QuestionInputController {
    private final QuestionInputInputBoundary questionInputUseCaseInteractor;

    public QuestionInputController(QuestionInputInputBoundary questionInputUseCaseInteractor) {
        this.questionInputUseCaseInteractor = questionInputUseCaseInteractor;
    }

    public void execute(String guessText) {

        QuestionInputInputData questionInputInputData = new QuestionInputInputData(guessText);

        questionInputUseCaseInteractor.execute(questionInputInputData);
    }
}
