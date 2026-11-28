package interface_adapter.game_running;

import use_case.load_question.LoadQuestionInputBoundary;

/**
 * The controller for the Load Question use case.
 */
public class LoadQuestionController {

    private final LoadQuestionInputBoundary loadQuestionUseCaseInteractor;

    public LoadQuestionController(LoadQuestionInputBoundary loadQuestionUseCaseInteractor) {
        this.loadQuestionUseCaseInteractor = loadQuestionUseCaseInteractor;
    }

    /**
     * Executes the Load Question use case.
     */
    public void execute() {
        loadQuestionUseCaseInteractor.execute();
    }
}
