package entity;

import static java.lang.Math.max;

public class Monster {
    private String name;
    private String description;
    private int health;
    private int maxHealth;
    private int armourClass;
    //private final Stats stats;

    public Monster(String name, String description, int maxHealth) {
        this.name = name;
        this.description = description;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public void takeDamage(int damage) {
        health = max(0, health - damage);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public String getName() {
        return name;
    }

    public int getCurrentHealth() {
        return health;
    }

}
