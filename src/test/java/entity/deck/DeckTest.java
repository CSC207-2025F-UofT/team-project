package entity.deck;

import entity.Player;
import entity.card.Card;
import entity.card.CharacterCard;
import entity.card.SorceryCard;
import entity.card.SpellCard;
import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeckTest {
    private final Deck deck = new Deck();
    private final Player player = new Player("Player 1");

    @Test
    void drawsTheMostRecentlyAddedCardFirst() {
        Card character = new CharacterCard(player, "Aki", 200, 3);
        Card spell = new SpellCard(player, "Fireball");
        deck.addCard(character);
        deck.addCard(spell);

        assertSame(spell, deck.drawCard());
        assertSame(character, deck.drawCard());
        assertThrows(EmptyStackException.class, deck::drawCard);
    }

    @Test
    void shufflePreservesTheCards() {
        Card character = new CharacterCard(player, "Kia", 200, 3);
        Card spell = new SpellCard(player, "Magic Missiles");
        Card sorcery = new SorceryCard(player, "Mirror", true);
        deck.addCard(character);
        deck.addCard(spell);
        deck.addCard(sorcery);

        deck.shuffle();

        assertEquals(Set.of(character, spell, sorcery), Set.of(deck.drawCard(), deck.drawCard(), deck.drawCard()));
        assertThrows(EmptyStackException.class, deck::drawCard);
    }
}
