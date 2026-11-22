package entity;

import entity.board.Direction;
import entity.card.CharacterCard;
import entity.card.SpellCard;

public class PendingCast {
    private final Player player;
    private final CharacterCard character;
    private final int x;
    private final int y;
    private final SpellCard spell;
    private final Direction direction;

    public PendingCast(Player player, CharacterCard character,
                       int x, int y, SpellCard spell,
                       Direction direction) {
        this.player = player;
        this.character = character;
        this.x = x;
        this.y = y;
        this.spell = spell;
        this.direction = direction;
    }

    public Player getPlayer() {
        return player;
    }

    public CharacterCard getCharacter() {
        return character;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public SpellCard getSpell() {
        return spell;
    }

    public Direction getDirection() {
        return direction;
    }
}
