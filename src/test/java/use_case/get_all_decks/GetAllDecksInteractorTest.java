package use_case.get_all_decks;

import entity.Deck;
import entity.Flashcard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GetAllDecksInteractorTest {

    private static class TestMockDeckDataAccessObject implements GetAllDecksDataAccessInterface{
        private final List<Deck> decks;

        public TestMockDeckDataAccessObject(List<Deck> decks) {
            this.decks = decks;
        }

        @Override
        public List<Deck> getAllDecks() {
            return decks;
        }

        @Override
        public boolean isEmpty() { return decks.isEmpty(); }
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

        GetAllDecksInputData inputData = new GetAllDecksInputData();
        GetAllDecksDataAccessInterface decks = new TestMockDeckDataAccessObject(list);

        GetAllDecksOutputBoundary successPresenter = new GetAllDecksOutputBoundary() {
            @Override
            public void prepareSuccessView(GetAllDecksOutputData decks) {
                Map<String, Integer> map = new HashMap<>(Map.of(
                        "CSC207H1", 3,
                        "MAT257Y1", 2
                ));
                assertEquals(map, decks.getDecks());
            }

            @Override
            public void prepareEmptyView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToDeckView() { /* not implemented yet */ }
        };

        GetAllDecksInputBoundary interactor = new GetAllDecksInteractor(decks, successPresenter);
        interactor.execute(inputData);
    }


    @Test
    void noAvailableDecksTest() {
        GetAllDecksInputData inputData = new GetAllDecksInputData();
        GetAllDecksDataAccessInterface decks = new TestMockDeckDataAccessObject(new ArrayList<>());

        GetAllDecksOutputBoundary emptyPresenter = new GetAllDecksOutputBoundary() {
            @Override
            public void prepareSuccessView(GetAllDecksOutputData decks) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareEmptyView(String error) {
                assertNotNull(error);
            }

            @Override
            public void switchToDeckView() { /* not implemented yet */ }
        };

        GetAllDecksInputBoundary interactor = new GetAllDecksInteractor(decks, emptyPresenter);
        interactor.execute(inputData);
    }
}
