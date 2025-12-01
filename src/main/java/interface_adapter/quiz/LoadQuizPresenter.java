package interface_adapter.quiz;

import use_case.loadQuiz.LoadQuizOutputBoundary;
import use_case.loadQuiz.LoadQuizOutputData;

public class LoadQuizPresenter implements LoadQuizOutputBoundary {

    private final Quiz_ViewModel viewModel;

    public LoadQuizPresenter(Quiz_ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(LoadQuizOutputData data) {
        Quiz_State state = viewModel.getState();
        state.setQuizId(data.getQuizId());
        state.setQuestionText(data.getQuestionText());
        state.setOptionTexts(data.getOptionTexts());

        // Fire property change to notify view
        viewModel.firePropertyChange();
    }
}
