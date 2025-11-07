package quiz_system.interface_adapters;

import quiz_system.usecase.SubmitQuizOutputBoundary;
import quiz_system.usecase.SubmitQuizOutputData;

public class QuizPresenter implements SubmitQuizOutputBoundary {

    private final QuizViewModel viewModel;

    public QuizPresenter(QuizViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(SubmitQuizOutputData data) {
        viewModel.quizId = data.getQuizId();
        viewModel.isCompleted = data.isCompleted();
        viewModel.status = data.getStatus();
        viewModel.feedbackMessage = data.getMessage();
    }
}
