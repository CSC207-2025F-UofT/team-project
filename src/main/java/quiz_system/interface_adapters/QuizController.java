package quiz_system.interface_adapters;

import quiz_system.usecase.SubmitQuizInputBoundary;
import quiz_system.usecase.SubmitQuizInputData;

public class QuizController {
    private final SubmitQuizInputBoundary submitUseCase;

    public QuizController(SubmitQuizInputBoundary submitUseCase) {
        this.submitUseCase = submitUseCase;
    }

    /**
     * @param selectedOptionIdOrNull  pass null if user didn't pick an option
     */
    public void onSubmit(int quizId, Integer selectedOptionIdOrNull) {
        SubmitQuizInputData input = new SubmitQuizInputData(quizId, selectedOptionIdOrNull);
        submitUseCase.submit(input);
    }
}
