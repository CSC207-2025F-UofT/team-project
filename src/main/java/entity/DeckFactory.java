package entity;

import entity.card.CardColor;
import entity.card.DrawTwoCard;
import entity.card.NumberCard;
import entity.card.ReverseCard;
import entity.card.SkipCard;
import entity.card.WildCard;
import entity.card.WildDrawFourCard;

/**
 * Factory for creating common Deck objects
 */
public class DeckFactory {

    private DeckFactory() {
        // Prevent instantiation.
    }

    public Deck createStandardDeck() {
        Deck deck = new Deck();

        for (CardColor color : CardColor.values()) {
            deck.addCard(new NumberCard(color, 0));

            for (int number = 1; number <= 9; number++) {
                deck.addCard(new NumberCard(color, number));
                deck.addCard(new NumberCard(color, number));
            }

            for (int i = 1; i <= 2; i++) {
                deck.addCard(new SkipCard(color));
                deck.addCard(new ReverseCard(color));
                deck.addCard(new DrawTwoCard(color));
            }
        }

        for (int i = 0; i < 4; i++) {
            deck.addCard(new WildCard());
            deck.addCard(new WildDrawFourCard());
        }

        return deck;
    }
}