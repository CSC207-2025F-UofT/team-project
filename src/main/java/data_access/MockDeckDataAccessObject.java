package data_access;

import entity.Deck;
import entity.DeckFactory;
import entity.Flashcard;
import use_case.get_all_decks.GetAllDecksDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * DeckDataAccessObject used for testing, gives mock data
 */
public class MockDeckDataAccessObject implements GetAllDecksDataAccessInterface {

    public MockDeckDataAccessObject(DeckFactory deckFactory) {}

    private void mockDataAddFlashcards(Deck deck) {
        deck.addFlashcard(new Flashcard("question1", "answer1"));
        deck.addFlashcard(new Flashcard("question2", "answer2"));
        deck.addFlashcard(new Flashcard("question3", "answer3"));
    }

    private Deck mockDataMakeDeck(String name) {
        Deck deck = new Deck(name);
        mockDataAddFlashcards(deck);
        return deck;
    }

    public List<Deck> getAllDecks() {
        List<Deck> list = new ArrayList<>();

        list.add(mockDataMakeDeck("CSC207H1"));
        list.add(mockDataMakeDeck("MAT257Y1"));
        list.add(mockDataMakeDeck("CSC148H1"));
        list.add(mockDataMakeDeck("CSC236H1"));
        list.add(mockDataMakeDeck("CSC240H1"));
        list.add(mockDataMakeDeck("CSC258H1"));
        list.add(mockDataMakeDeck("MAT267H1"));
        list.add(mockDataMakeDeck("CSC209H1"));
        list.add(mockDataMakeDeck("MAT237Y1"));

        // to test scroll bar
        list.add(mockDataMakeDeck("1"));
        list.add(mockDataMakeDeck("2"));
        list.add(mockDataMakeDeck("3"));
        list.add(mockDataMakeDeck("4"));
        list.add(mockDataMakeDeck("5"));
        list.add(mockDataMakeDeck("6"));
        list.add(mockDataMakeDeck("7"));
        list.add(mockDataMakeDeck("8"));
        list.add(mockDataMakeDeck("9"));
        list.add(mockDataMakeDeck("10"));
        list.add(mockDataMakeDeck("11"));
        list.add(mockDataMakeDeck("12"));

        return list;
    }

    public boolean isEmpty() {
        return false;
    }
}

