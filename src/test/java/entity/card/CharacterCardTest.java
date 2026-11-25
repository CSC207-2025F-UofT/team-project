package entity.card;

import entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterCardTest {
    private final CharacterCard character = new CharacterCard(new Player("Player 1"), "Aki", 200, 3);

    @Test
    void clampsDamageAndHealingToHealthBounds() {
        character.takeDamage(50);
        assertEquals(150, character.getHP());

        character.takeDamage(300);
        assertEquals(0, character.getHP());

        character.takeDamage(-300);
        assertEquals(200, character.getHP());
    }

    @Test
    void reviveRestoresFullHealthOnlyAfterKnockout() {
        character.takeDamage(50);
        character.revive();
        assertEquals(150, character.getHP());

        character.takeDamage(150);
        character.revive();
        assertEquals(200, character.getHP());
    }
}
