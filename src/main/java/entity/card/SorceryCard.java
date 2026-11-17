package entity.card;

public class SorceryCard extends Card {
    private final boolean reaction;

    public SorceryCard(String owner, String name, boolean reaction) {
        super(owner, name);
        this.reaction = reaction;
    }

    public boolean isReaction() {
        return reaction;
    }
}
