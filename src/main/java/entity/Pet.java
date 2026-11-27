package entity;

public class Pet {

    public enum Species {
        DOG,
        CAT
    }

    private final String owner;
    private final String name;
    private final Species species;
    private final String picturesURL;
    private int hunger;
    private int happiness;
    private int health;

    public Pet(String owner, String name,
               Species species, String URL) {
        this(owner, name, species, URL, 5, 5, 10);
    }

    public Pet(String owner, String name,
               Species species, String URL,
               int hunger, int happiness, int health) {

        if (hunger < 0 || hunger > 10
                || happiness < 0 || happiness > 10
                || health < 0 || health > 10) {
            throw new IllegalArgumentException(
                    "Pet stats must be between 0 and 10.");
        }

        this.owner = owner;
        this.name = name;
        this.species = species;
        this.picturesURL = URL;
        this.hunger = hunger;
        this.happiness = happiness;
        this.health = health;
    }

    public int getHunger() {
        return hunger;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getHealth() {
        return health;
    }

    public void feed(int amount) {
        requirePositive(amount);
        hunger -= Math.min(amount, hunger);
    }

    public void play(int amount) {
        requirePositive(amount);
        happiness += Math.min(amount, 10 - happiness);
    }

    public void heal(int amount) {
        requirePositive(amount);
        health += Math.min(amount, 10 - health);
    }

    private void requirePositive(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "The amount must be positive."
            );
        }
    }
    public String getOwnerUsername() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public Species getSpecies() {
        return species;
    }

    public String getImageUrl() {
        return picturesURL;
    }

    }
