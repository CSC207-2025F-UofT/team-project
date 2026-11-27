package interface_adapter.game_running;

import use_case.question_input.QuestionInputOutputBoundary;
import use_case.question_input.QuestionInputOutputData;

public class QuestionInputPresenter implements QuestionInputOutputBoundary {
    private final GameRunningViewModel gameRunningViewModel;

    public QuestionInputPresenter(GameRunningViewModel gameRunningViewModel) {
        this.gameRunningViewModel = gameRunningViewModel;
    }
    /**
     * Prepares the success view for the Question input Use Case.
     * @param outputData the output data
     */
    public void prepareSuccessView(QuestionInputOutputData outputData) {

    }

    /**
     * Prepares the failure view for the Question input Use Case.
     * @param errorMessage the explanation of the failure
     */
    public void prepareFailView(String errorMessage) {
    }
}
