package entities;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private ArrayList<Card> flashcardDeck;
    private boolean studying;
    private ArrayList<Card> studyingDeck;
    private Card currentCard;
    String title;

    public Deck(String title, ArrayList<Card> flashcardDeck) {
        this.title = title;
        this.flashcardDeck = flashcardDeck;
        studying = false;
    }

    private void startStudying() {
        studying = true;
        studyingDeck = new ArrayList<>(flashcardDeck);
    }

    private void stopStudying() {
        studying = false;
        studyingDeck = null;
    }

    public Card getCard() {
        // if we've finished the deck,
        if (studyingDeck == null) {
            currentCard = null;
            stopStudying();
            return null;
        }
        if (!studying) {
            startStudying();
        }
        Random r = new Random();
        int num = r.nextInt(studyingDeck.size());
        return studyingDeck.get(num);
    }
}
