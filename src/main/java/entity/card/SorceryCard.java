package entity.card;

import entity.Player;

public class SorceryCard extends Card {
    private final boolean reaction;

    public SorceryCard(Player owner, String name, boolean reaction) {
        super(owner, name);
        this.reaction = reaction;
    }

    public boolean isReaction() {
        return reaction;
    }
}
