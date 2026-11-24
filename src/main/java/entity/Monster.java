package entity;

import static java.lang.Math.max;

public class Monster {
    private String name;
    private String index;
    private int health;
    private int maxHealth;
    private int armourClass;
    private final Stats stats;

    public Monster(String name, String index, int maxHealth, int armourClass, Stats stats) {
        this.name = name;
        this.index = index;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.armourClass = armourClass;
        this.stats = stats;
    }

    @Override
    public String toString() {return name;}

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

    public int getArmourClass() {return armourClass;}

    public Stats getStats() {return stats;}
}
