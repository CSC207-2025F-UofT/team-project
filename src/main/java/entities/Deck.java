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
        currentCard = null;
    }

    public Card getCard() {
        // if we've finished the deck, stop
        if (studyingDeck == null) {
            stopStudying();
            return null;
        }
        //if this is the first card, turn on studying
        if (!studying) {
            startStudying();
        }
        // if the deck has length 1, return the only card in it
        if (studyingDeck.size() == 1) {
            currentCard = studyingDeck.get(0);
            return studyingDeck.get(0);
        }
        // repeatedly search for a card that isn't the same as the current one
        // the deck shouldn't be null here, but currentCard might be
        Random r = new Random();
        int num = r.nextInt(studyingDeck.size());
        while (studyingDeck.get(num) == currentCard) {
            num = r.nextInt(studyingDeck.size());
        }
        currentCard = studyingDeck.get(num);
        return studyingDeck.get(num);
    }
    public void addCard(Card card){
        flashcardDeck.add(card);

    }

    public void removeCurrentCard() {
        studyingDeck.remove(currentCard);
    }

    public Card getCurrentCard() {
        return currentCard;
    }
    public String getTitle(){return this.title;}
    public ArrayList<Card> getFlashcardDeck(){ return this.flashcardDeck;}
    public void setTitle(String title){ this.title = title;}
}
