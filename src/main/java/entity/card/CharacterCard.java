package entity.card;

public class CharacterCard extends Card {
    private final int HP;
    private int currentHP;
    private final int speed;
    private boolean KOd;

    public CharacterCard(String owner, String name, int hp, int speed) {
        super(owner, name);
        this.HP = hp;
        this.currentHP = hp;
        this.speed = speed;
        this.KOd = false;
    }

    /// Do damage to this char. KO's the char if this drops them below 0 HP. Un-KO's if healing a KO'd char.
    /// Removal from board (damage + KO) should be checked from a layer above, where this is called from.
    /// @param damage The damage to be taken by this Character (can be negative)
    public void takeDamage(int damage) {
        this.currentHP = Math.clamp(this.currentHP - damage, 0, this.HP);

        // KO this char
        if (this.currentHP == 0) {
            this.KOd = true;
        }

        // Un-KO this char (If KO'd and Healed)
        if (this.KOd && damage < 0) {
            this.KOd = false;
        }
    }

    /// 'Revive' this character at maximum HP. Only works if KO'd.
    public void revive() {
        if (this.KOd) {
            this.currentHP = this.HP;
            this.KOd = false;
        }
    }

    public int getHP() {
        return this.currentHP;
    }

    public int getSpeed() {
        return this.speed;
    }
}
