package interface_adapters.flashcards;

import usecases.GenerateFlashcardsOutputBoundary;
import usecases.GenerateFlashcardsResponseModel;

/**
 * Console presenter for flashcard generation.
 * Prints flashcards or error messages to the console for testing.
 */
public class GenerateFlashcardsPresenter implements GenerateFlashcardsOutputBoundary {

    @Override
    public void presentFlashcards(GenerateFlashcardsResponseModel responseModel) {
        System.out.println("\nFlashcards generated successfully:\n");
        responseModel.getFlashcardSet().getFlashcards().forEach(card -> {
            System.out.println("Q: " + card.getQuestion());
            System.out.println("A: " + card.getAnswer());
            System.out.println();
        });
    }

    @Override
    public void presentError(String message) {
        System.out.println("Error: " + message);
    }
}
