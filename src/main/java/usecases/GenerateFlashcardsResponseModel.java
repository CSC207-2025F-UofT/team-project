package usecases;

import entities.FlashcardSet;

public class GenerateFlashcardsResponseModel {
    private final FlashcardSet flashcardSet;

    public GenerateFlashcardsResponseModel(FlashcardSet flashcardSet) {
        this.flashcardSet = flashcardSet;
    }

    public FlashcardSet getFlashcardSet() {
        return flashcardSet;
    }
}