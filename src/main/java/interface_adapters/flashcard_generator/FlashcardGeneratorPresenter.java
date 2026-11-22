package interface_adapters.flashcard_generator;

import interface_adapters.ViewManagerModel;
import use_case.flashcard_generator.FlashcardGeneratorOutputBoundary;
import use_case.flashcard_generator.FlashcardGeneratorOutputData;

/**
 * Presenter class for the flashcard generator use case.
 */
public class FlashcardGeneratorPresenter implements FlashcardGeneratorOutputBoundary {

    private final FlashcardGeneratorViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public FlashcardGeneratorPresenter(FlashcardGeneratorViewModel viewModel, ViewManagerModel viewManagerModel){
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }
    @Override
    public void prepareFailView(String errorMessage) {

    }

    @Override
    public void prepareSuccessView(FlashcardGeneratorOutputData output) {

    }
}
