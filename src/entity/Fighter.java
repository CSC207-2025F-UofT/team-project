package entity;

public class Fighter {

    public final String name;
    public final boolean player;

    public final int maxHp;
    public final int attack;
    public final int defense;
    public final int speed;

    public int hp;
    public boolean guarding;

    public Fighter(
            String name,
            boolean player,
            int maxHp,
            int attack,
            int defense,
            int speed) {

        this.name = name;
        this.player = player;
        this.maxHp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;

        this.hp = maxHp;
        this.guarding = false;
    }

    public boolean alive() {
        return hp > 0;
    }

    public void takeDamage(int damage) {
        hp = Math.max(0, hp - damage);
    }

    public void defend() {
        guarding = true;
    }

    public void stopDefending() {
        guarding = false;
    }
}