package entity.deck;

import entity.Player;
import entity.card.*;

public class DeckFactory {
    public static Deck createDeck(Player owner) {
        Deck deck = new Deck();
        SpellCardFactory spellFactory = new SpellCardFactory();
        CharacterCardFactory charFactory = new CharacterCardFactory();
        SorceryCardFactory sorceryFactory = new SorceryCardFactory();

        addCopies(deck, charFactory, owner, "Aki", 6);
        addCopies(deck, charFactory, owner, "Kia", 6);
        addCopies(deck, spellFactory, owner, "Magic Missiles", 8);
        addCopies(deck, spellFactory, owner, "Fireball", 8);
        addCopies(deck, sorceryFactory, owner, "Mirror", 4);
        addCopies(deck, sorceryFactory, owner, "Portal", 4);

        deck.shuffle();

        return deck;
    }

    private static void addCopies(Deck deck, CardFactory cardFactory, Player owner, String name, int amount) {
        for (int i = 0; i <= amount; i++) {
            Card card = cardFactory.create(owner, name);
            deck.addCard(card);
        }
    }
}
