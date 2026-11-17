package entity.card;

public class SpellCardBuilder {
    private final SpellCard spellCard;

    public SpellCardBuilder(String owner, String name) {
        this.spellCard = new SpellCard(owner, name);
    }

    public SpellCardBuilder damage(int damage) {
        this.spellCard.setDamage(damage);
        return this;
    }

    public SpellCardBuilder range(int range) {
        this.spellCard.setRange(range);
        return this;
    }

    public SpellCardBuilder piercing() {
        this.spellCard.setPiercing(true);
        return this;
    }

    public SpellCardBuilder areaOfEffect(int areaOfEffect) {
        this.spellCard.setAoE(areaOfEffect);
        return this;
    }

    public SpellCardBuilder charges(int charges) {
        this.spellCard.setCharges(charges);
        return this;
    }

    public SpellCard build() {
        return this.spellCard;
    }
}


