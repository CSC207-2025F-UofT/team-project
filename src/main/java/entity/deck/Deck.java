package entity.deck;

import entity.card.Card;

import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class Deck {
    private Stack<Card> deck;
    private Random random = new Random();

    public Deck() {
        this.deck = new Stack<>();
    }

    public void shuffle(){
        Collections.shuffle(this.deck, this.random);
    }

    public void addCard(Card card){
        this.deck.push(card);
    }

    public Card drawCard(){
        return this.deck.pop();
    }
}
