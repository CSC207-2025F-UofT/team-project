package use_case.flashcard_studying;
import data_access.FlashcardDataAccessInterface;

public class FlashcardStudyingInteractor implements FlashcardStudyingInputBoundary {

    private final FlashcardStudyingOutputBoundary flashcardStudyingPresenter;
    private final FlashcardDataAccessInterface dataAccessObject;

    public FlashcardStudyingInteractor(FlashcardStudyingOutputBoundary presenter, FlashcardDataAccessInterface dao) {
        this.flashcardStudyingPresenter = presenter;
        this.dataAccessObject = dao;
    }

    public void execute(FlashcardStudyingInputData inputData) {

    }
}
