package entity.card;

public class SpellCard extends Card {
    private int range = 0;
    private int damage = 0;
    private boolean piercing = false;
    private int AoE = 0;
    private int charges = 0;

    public SpellCard(String owner, String name) {
        super(owner, name);
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isPiercing() {
        return piercing;
    }

    public void setPiercing(boolean piercing) {
        this.piercing = piercing;
    }

    public int getAoE() {
        return AoE;
    }

    public void setAoE(int aoE) {
        AoE = aoE;
    }

    public int getCharges() {
        return charges;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }
}
