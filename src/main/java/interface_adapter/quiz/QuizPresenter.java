package interface_adapter.quiz;

import interface_adapter.Battle.BattleState;
import interface_adapter.Battle.BattleViewModel;
import interface_adapter.ViewManagerModel;
import use_case.quiz.SubmitQuizOutputBoundary;
import use_case.quiz.SubmitQuizOutputData;

public class QuizPresenter implements SubmitQuizOutputBoundary {

    private final QuizViewModel viewModel;
    private final BattleViewModel battleViewModel;
    private final ViewManagerModel viewManagerModel;

    public QuizPresenter(QuizViewModel viewModel, BattleViewModel battleViewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.battleViewModel = battleViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void present(SubmitQuizOutputData data) {
        QuizState quizState = viewModel.getState();
        quizState.setQuizId();
        quizState.setCompleted(data.isCompleted());
        quizState.setStatus(data.getStatus());
        quizState.setFeedbackMessage(data.getMessage());

        // notify view of the state change
        viewModel.firePropertyChange();
    }

    @Override
    public void switchToBattleView() {
        QuizState quizState = viewModel.getState();
        boolean isCorrect = "CORRECT".equals(quizState.getStatus());

        BattleState battleState = battleViewModel.getState();
        battleState.setQuizResult(isCorrect);
        battleState.setJustFinishedQuiz(true);

        // HACK: switch to battle view first then update battle's attributes
        viewManagerModel.setState(battleViewModel.getViewName());
        viewManagerModel.firePropertyChange();

        battleViewModel.setState(battleState);
        battleViewModel.firePropertyChange();
    }
}
