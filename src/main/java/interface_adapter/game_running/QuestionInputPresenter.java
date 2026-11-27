package interface_adapter.game_running;

import interface_adapter.ViewManagerModel;
import interface_adapter.game_summary.GameSummaryViewModel;
import use_case.question_input.QuestionInputOutputBoundary;
import use_case.question_input.QuestionInputOutputData;

public class QuestionInputPresenter implements QuestionInputOutputBoundary {
    private final GameRunningViewModel gameRunningViewModel;
    private final ViewManagerModel viewManagerModel;

    public QuestionInputPresenter(ViewManagerModel viewManagerModel, GameRunningViewModel gameRunningViewModel) {
        this.gameRunningViewModel = gameRunningViewModel;
        this.viewManagerModel = viewManagerModel;
    }
    /**
     * Prepares the success view for the Question input Use Case.
     * @param outputData the output data
     */
    public void prepareSuccessView(QuestionInputOutputData outputData) {
        System.out.println("suck");
    }

    /**
     * Prepares the failure view for the Question input Use Case.
     * @param errorMessage the explanation of the failure
     */
    public void prepareFailView(String errorMessage) {
        System.out.println("fail");
    }
}
