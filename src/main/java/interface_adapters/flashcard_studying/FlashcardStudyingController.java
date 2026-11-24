package interface_adapters.flashcard_studying;
import use_case.flashcard_studying.FlashcardStudyingInteractor;
import use_case.flashcard_studying.FlashcardStudyingInputBoundary;
import use_case.flashcard_studying.FlashcardStudyingInputData;

public class FlashcardStudyingController {
    private final FlashcardStudyingInputBoundary interactor;

    public FlashcardStudyingController(FlashcardStudyingInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String button) {
        FlashcardStudyingInputData inputData = new FlashcardStudyingInputData(button);
        interactor.execute(inputData);
    }

    public void execute(String button, String title) {
        FlashcardStudyingInputData inputData = new FlashcardStudyingInputData(button, title);
        interactor.execute(inputData);

    }
}
