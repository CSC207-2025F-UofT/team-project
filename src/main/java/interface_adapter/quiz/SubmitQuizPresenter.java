package interface_adapter.quiz;

import interface_adapter.Battle.BattleState;
import interface_adapter.Battle.BattleViewModel;
import interface_adapter.ViewManagerModel;
import use_case.submitQuiz.SubmitQuizOutputBoundary;
import use_case.submitQuiz.SubmitQuizOutputData;

public class SubmitQuizPresenter implements SubmitQuizOutputBoundary {

    private final QuizViewModel viewModel;
    private final BattleViewModel battleViewModel;
    private final ViewManagerModel viewManagerModel;

    public SubmitQuizPresenter(QuizViewModel viewModel, BattleViewModel battleViewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.battleViewModel = battleViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void present(SubmitQuizOutputData data) {
        QuizState quizState = viewModel.getState();
        quizState.setCompleted(data.isCompleted());
        quizState.setStatus(data.getStatus());
        quizState.setFeedbackMessage(data.getMessage());

        // notify view of the state change
        viewModel.firePropertyChange();
    }

    @Override
    public void switchToBattleView(boolean isCorrect) {
        BattleState battleState = battleViewModel.getState();
        battleState.setQuizResult(isCorrect);
        battleState.setJustFinishedQuiz(true);

        viewManagerModel.setState(battleViewModel.getViewName());
        viewManagerModel.firePropertyChange();

        battleViewModel.setState(battleState);
        battleViewModel.firePropertyChange();
    }
}
