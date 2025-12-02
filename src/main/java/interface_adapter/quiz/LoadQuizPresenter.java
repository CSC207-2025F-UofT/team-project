package interface_adapter.quiz;

import use_case.loadQuiz.LoadQuizOutputBoundary;
import use_case.loadQuiz.LoadQuizOutputData;

public class LoadQuizPresenter implements LoadQuizOutputBoundary {

    private final QuizViewModel viewModel;

    public LoadQuizPresenter(QuizViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(LoadQuizOutputData data) {
        QuizState state = viewModel.getState();

        state.setQuestionText(data.getQuestionText());
        state.setOptionTexts(data.getOptionTexts());

        state.setStatus(null);
        state.setFeedbackMessage(null);

        // Fire property change to notify view
        viewModel.firePropertyChange();
    }
}
