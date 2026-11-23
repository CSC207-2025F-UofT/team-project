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

    public Pet (String owner, String name,
                Species species, String URL) {

    this.owner = owner;
    this.name = name;
    this.species = species;
    this.picturesURL = URL;

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
