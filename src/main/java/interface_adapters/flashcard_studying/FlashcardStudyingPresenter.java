package interface_adapters.flashcard_studying;
import use_case.flashcard_studying.FlashcardStudyingOutputBoundary;
import use_case.flashcard_studying.FlashcardStudyingOutputData;



public class FlashcardStudyingPresenter implements FlashcardStudyingOutputBoundary {

    private final FlashcardStudyingViewModel viewModel;

    public FlashcardStudyingPresenter(FlashcardStudyingViewModel viewModel) {
        this.viewModel = viewModel;
    }
    public void prepareSuccessView(FlashcardStudyingOutputData outputData) {
        final FlashcardStudyingState studyingState = viewModel.getState();
        studyingState.setText(outputData.getText());
        viewModel.notifyStateChanged();
    }

    public void prepareFailView(String message) {
        final FlashcardStudyingState studyingState = viewModel.getState();
        studyingState.setError(message);
        viewModel.notifyStateChanged();
    }

}
