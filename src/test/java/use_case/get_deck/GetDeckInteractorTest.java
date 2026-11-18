package use_case.get_deck;

import entity.Deck;
import entity.Flashcard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GetDeckInteractorTest {

    private static class TestMockDeckDataAccessObject implements GetDeckDataAccessInterface {
        private List<Deck> decks;

        public TestMockDeckDataAccessObject(List<Deck> decks) {
            this.decks = decks;
        }

        @Override
        public boolean deckExists(String name) {
            for (Deck deck : decks) {
                if (deck.getName().equals(name)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Deck getDeck(String name) {
            for (Deck deck : decks) {
                if (deck.getName().equals(name)) {
                    return deck;
                }
            }
            return null;
        }
    }

    @Test
    void successTest() {
        List<Deck> list = new ArrayList<>();
        Deck deck1 = new Deck("CSC207H1");
        deck1.addFlashcard(new Flashcard("question1", "answer1"));
        deck1.addFlashcard(new Flashcard("question2", "answer2"));
        deck1.addFlashcard(new Flashcard("question3", "answer3"));

        Deck deck2 = new Deck("MAT257Y1");
        deck2.addFlashcard(new Flashcard("a", "b"));
        deck2.addFlashcard(new Flashcard("c", "d"));

        list.add(deck1);
        list.add(deck2);

        GetDeckInputData inputData = new GetDeckInputData("CSC207H1", "LIST");
        GetDeckDataAccessInterface deck = new TestMockDeckDataAccessObject(list);

        GetDeckOutputBoundary successPresenter = new GetDeckOutputBoundary() {
            @Override
            public void prepareSuccessView(GetDeckOutputData deck) {
                assertEquals("CSC207H1", deck.getName());

                List<Map<String, String>> flashcards = new ArrayList<>();
                flashcards.add(new HashMap<>(Map.of("question1", "answer1")));
                flashcards.add(new HashMap<>(Map.of("question2", "answer2")));
                flashcards.add(new HashMap<>(Map.of("question3", "answer3")));

                assertEquals(flashcards, deck.getFlashcards());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void prepareEmptyView(String error) { fail("Use case success is unexpected."); }
        };

        GetDeckInputBoundary interactor = new GetDeckInteractor(deck, successPresenter);
        interactor.execute(inputData);
    }


    @Test
    void noAvailableDeckTest() {
        GetDeckInputData inputData = new GetDeckInputData(null, "STUDY");
        GetDeckDataAccessInterface decks = new TestMockDeckDataAccessObject(new ArrayList<>());

        GetDeckOutputBoundary emptyPresenter = new GetDeckOutputBoundary() {
            @Override
            public void prepareSuccessView(GetDeckOutputData decks) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertNotNull(error);
            }

            @Override
            public void prepareEmptyView(String error) { fail("Use case success is unexpected."); }
        };

        GetDeckInputBoundary interactor = new GetDeckInteractor(decks, emptyPresenter);
        interactor.execute(inputData);
    }
}

