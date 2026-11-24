package interface_adapter.game_running;

import interface_adapter.ViewManagerModel;
import use_case.load_question.LoadQuestionOutputBoundary;
import use_case.load_question.LoadQuestionOutputData;

/**
 * The Presenter for the Load Question Use Case.
 */
public class LoadQuestionPresenter implements LoadQuestionOutputBoundary {
    private final GameRunningViewModel gameRunningViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoadQuestionPresenter(ViewManagerModel viewManagerModel, GameRunningViewModel gameRunningViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.gameRunningViewModel = gameRunningViewModel;
    }

    @Override
    public void prepareSuccessView(LoadQuestionOutputData response) {
        // On success, update the GameRunningViewModel's state
        final GameRunningState state = gameRunningViewModel.getState();
        state.setQuestionText(response.getQuestionText());
        state.setAnswerCount(response.getAnswerCount());
        gameRunningViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final GameRunningState state = gameRunningViewModel.getState();
        state.setLoadQuestionError(error);
        gameRunningViewModel.notifyStateChanged();
    }
}
