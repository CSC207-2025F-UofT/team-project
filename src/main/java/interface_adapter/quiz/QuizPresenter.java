package interface_adapter.quiz;

import interface_adapter.Battle.Battle_ViewModel;
import interface_adapter.ViewManagerModel;
import use_case.quiz.SubmitQuizOutputBoundary;
import use_case.quiz.SubmitQuizOutputData;

public class QuizPresenter implements SubmitQuizOutputBoundary {

    private final Quiz_ViewModel viewModel;
    private final Battle_ViewModel battleViewModel;
    private final ViewManagerModel viewManagerModel;

    public QuizPresenter(Quiz_ViewModel viewModel, Battle_ViewModel battleViewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.battleViewModel = battleViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void present(SubmitQuizOutputData data) {
        Quiz_State quizState = viewModel.getState();
        quizState.setQuizId(data.getQuizId());
        quizState.setCompleted(data.isCompleted());
        quizState.setStatus(data.getStatus());
        quizState.setFeedbackMessage(data.getMessage());

        // notify view of the state change
        viewModel.firePropertyChange();
    }

    @Override
    public void switchToBattleView() {
        viewManagerModel.setState(battleViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
