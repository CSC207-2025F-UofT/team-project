package use_case.flashcard_generator;


import data_access.FlashcardDAO;
import data_access.FlashcardDataAccessInterface;
import entities.Card;
import entities.Deck;
import interface_adapters.flashcard_generator.FlashcardGeneratorPresenter;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FlashcardGeneratorInteractorTest {
    void successTest() {
        List<FlashcardGeneratorInputData.TextPiece> textPieces = new ArrayList<>();

        textPieces.add(new FlashcardGeneratorInputData.TextPiece("What course is this project for?", true));

        textPieces.add(new FlashcardGeneratorInputData.TextPiece("CSC207", false));

        FlashcardGeneratorInputData inputData = new FlashcardGeneratorInputData(textPieces);
        FlashcardDataAccessInterface flashcardRepository = new FlashcardDAO();

        FlashcardGeneratorOutputBoundary flashcardPresenter = new FlashcardGeneratorOutputBoundary() {
            @Override
            public void prepareSuccessView(FlashcardGeneratorOutputData data) {
                Deck deck = data.getDeck();
                assertEquals("Temp title", data.getTitle());
                assertEquals(1, deck.getFlashcardDeck().size());

                assertEquals("What course is this project for?", deck.getFlashcardDeck().get(0).getQuestion());
                assertEquals("CSC207", deck.getFlashcardDeck().get(0).getAnswer());

            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        FlashcardGeneratorInputBoundary interactor = new FlashcardGeneratorInteractor(flashcardRepository, flashcardPresenter);
        interactor.execute(inputData);
    }


    @Test
    void failureEmptyInputTest() {
        List<FlashcardGeneratorInputData.TextPiece> textPieces = new ArrayList<>();
        FlashcardGeneratorInputData inputData = new FlashcardGeneratorInputData(textPieces);
        FlashcardDataAccessInterface flashcardRepository = new FlashcardDAO();





        FlashcardGeneratorOutputBoundary flashcardPresenter = new FlashcardGeneratorOutputBoundary() {
            @Override
            public void prepareSuccessView(FlashcardGeneratorOutputData data) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Incorrect format for \"Flashcard\".", error);
            }
        };

        FlashcardGeneratorInputBoundary interactor = new FlashcardGeneratorInteractor(flashcardRepository, flashcardPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureUserDoesNotExistTest() {
        List<FlashcardGeneratorInputData.TextPiece> textPieces = new ArrayList<>();
       FlashcardGeneratorInputData inputData = new FlashcardGeneratorInputData(textPieces);
        FlashcardDataAccessInterface flashcardRepository = new FlashcardDAO();


        FlashcardGeneratorOutputBoundary flashcardPresenter = new FlashcardGeneratorOutputBoundary() {
            @Override
            public void prepareSuccessView(FlashcardGeneratorOutputData data) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Paul: Account does not exist.", error);
            }
        };

        FlashcardGeneratorInputBoundary interactor = new FlashcardGeneratorInteractor(flashcardRepository, flashcardPresenter);
        interactor.execute(inputData);
    }
}
