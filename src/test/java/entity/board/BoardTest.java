package entity.board;

import entity.Player;
import entity.card.CharacterCard;
import entity.card.SorceryCard;
import entity.card.SpellCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {
    private final Board board = new Board();
    private final Player player = new Player("Player 1");

    @Test
    void movesCharacterToAnotherTileAndClearsSource() {
        CharacterCard character = new CharacterCard(player, "Aki", 200, 3);
        board.playCard(1, 2, character);

        board.moveTile(1, 2, 4, 5);

        assertTrue(board.getTile(1, 2).isEmpty());
        assertSame(character, board.getTile(4, 5).getCharacterCard());
    }

    @Test
    void rejectsCharacterPlacementOnAnOccupiedTile() {
        SorceryCard mirror = new SorceryCard(player, "Mirror", true);
        CharacterCard character = new CharacterCard(player, "Kia", 200, 3);
        board.playCard(2, 4, mirror);

        assertThrows(IllegalMoveException.class, () -> board.playCard(2, 4, character));
        assertSame(mirror, board.getTile(2, 4).getSorceryCard());
    }

    @Test
    void rejectsEquippingASpellToAnEmptyTile() {
        SpellCard spell = new SpellCard(player, "Fireball");

        assertThrows(IllegalMoveException.class, () -> board.playCard(0, 3, spell));
        assertTrue(board.getTile(0, 3).isEmpty());
    }

    @Test
    void scoresCurrentHealthForEachPlayersCharacters() {
        Player opponent = new Player("Player 2");
        CharacterCard damaged = new CharacterCard(player, "Aki", 200, 3);
        board.playCard(0, 0, damaged);
        board.playCard(5, 5, new CharacterCard(player, "Cel", 150, 4));
        board.playCard(2, 3, new CharacterCard(opponent, "Kia", 200, 3));
        damaged.takeDamage(75);

        assertEquals(275, board.calculateScore(player));
        assertEquals(200, board.calculateScore(opponent));
    }
}
