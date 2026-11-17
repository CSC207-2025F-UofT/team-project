package use_case_layer.flashcard_generator;

import interface_adapters.flashcard_generator.FlashcardGeneratorPresenter;

import java.util.List;

public class FlashcardGeneratorInteractor implements FlashcardGeneratorInputBoundary {

    private final FlashcardGeneratorOutputBoundary presenter;

    public FlashcardGeneratorInteractor(FlashcardGeneratorOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(FlashcardGeneratorInputData data) {

    }
}
