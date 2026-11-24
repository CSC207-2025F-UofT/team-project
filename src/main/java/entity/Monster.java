package entity;

import static java.lang.Math.max;

public class Monster {
    private String name;
    private String description;
    private int currentHealth;
    private int maxHealth;

    public Monster(String name, String description, int maxHealth) {
        this.name = name;
        this.description = description;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public void takeDamage(int damage) {
        currentHealth = max(0, currentHealth - damage);
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public String getName() {
        return name;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

}
