package entity;

import static java.lang.Math.max;

public class PlayerCharacter {
    private String name;
    private int health;
    private int maxHealth;


    public PlayerCharacter(String name, int health, int maxHealth) {
        this.name = name;
        this.health = health;
        this.maxHealth = maxHealth;
    }

    public void takeDamage(int damage) {
        health = max(0, health - damage);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getHealth() {
        return health;
    }
}
